package org.jgroups.blocks;


import org.jgroups.Address;
import org.jgroups.Global;
import org.jgroups.JChannel;
import org.jgroups.View;
import org.jgroups.protocols.FRAG;
import org.jgroups.protocols.FRAG2;
import org.jgroups.protocols.TP;
import org.jgroups.stack.Protocol;
import org.jgroups.tests.ChannelTestBase;
import org.jgroups.util.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A collection of tests to test the RpcDispatcher.
 * 
 * NOTE on processing return values: 
 * 
 * The method RspDispatcher.callRemoteMethods(...) returns an RspList, containing one Rsp
 * object for each group member receiving the RPC call. Rsp.getValue() returns the 
 * value returned by the RPC call from the corresponding member. Rsp.getValue() may
 * contain several classes of values, depending on what happened during the call:
 * 
 * (i) a value of the expected return data type, if the RPC call completed successfully 
 * (ii) null, if the RPC call timed out before the value could be returned
 * (iii) an object of type java.lang.Throwable, if an exception (e.g. lava.lang.OutOfMemoryException) 
 * was raised during the processing of the call 
 * 
 * It is wise to check for such cases when processing RpcDispatcher calls.
 * 
 * This also applies to the return value of callRemoteMethod(...).
 * 
 * @author Bela Ban
 */
@Test(groups=Global.STACK_DEPENDENT,sequential=true)
public class RpcDispatcherTest extends ChannelTestBase {
    RpcDispatcher disp1, disp2, disp3;
    JChannel c1, c2, c3;

    // specify return values sizes which should work correctly with 64Mb heap
    final static int[] SIZES={10000, 20000, 40000, 80000, 100000, 200000, 400000, 800000,
        1000000, 2000000, 5000000};

    @BeforeMethod
    protected void setUp() throws Exception {
        c1=createChannel(true, 3);
        c1.setName("A");
        final String GROUP="RpcDispatcherTest";
        disp1=new RpcDispatcher(c1, new ServerObject(1));
        c1.connect(GROUP);

        c2=createChannel(c1);
        c2.setName("B");
        disp2=new RpcDispatcher(c2, new ServerObject(2));
        c2.connect(GROUP);

        c3=createChannel(c1);
        c3.setName("C");
        disp3=new RpcDispatcher(c3, new ServerObject(3));
        c3.connect(GROUP);

        System.out.println("c1.view=" + c1.getView() + "\nc2.view=" + c2.getView() + "\nc3.view=" + c3.getView());
        Util.waitUntilAllChannelsHaveSameSize(30000, 1000, c1, c2, c3);
        View view=c3.getView();
        assert view.size() == 3 : "view=" + view;
    }

    @AfterMethod
    protected void tearDown() throws Exception {
        disp3.stop();
        disp2.stop();
        disp1.stop();
        Util.close(c3, c2, c1);
    }

    public void testEmptyConstructor() throws Exception {
        RpcDispatcher d1=new RpcDispatcher(), d2=new RpcDispatcher();
        JChannel channel1=null, channel2=null;

        final String GROUP="RpcDispatcherTest";
        try {
            channel1=createChannel(true, 2);
            channel2=createChannel(channel1);
            d1.setChannel(channel1);
            d2.setChannel(channel2);
            d1.setServerObject(new ServerObject(1));
            d2.setServerObject(new ServerObject(2));
            d1.start();
            d2.start();
            channel1.connect(GROUP);
            channel2.connect(GROUP);

            Util.sleep(500);

            View view=channel2.getView();
            System.out.println("view channel 2= " + view);

            view=channel1.getView();
            System.out.println("view channel 1= " + view);

            assert view.size() == 2;
            RspList<Integer> rsps=d1.callRemoteMethods(null, "foo", null, null, new RequestOptions(ResponseMode.GET_ALL, 5000));
            System.out.println("rsps:\n" + rsps);
            assert rsps.size() == 2;
            for(Rsp<Integer> rsp: rsps.values()) {
                assert rsp.wasReceived();
                assert !rsp.wasSuspected();
                assert rsp.getValue() != null;
            }


            Object server_object=new Object() {
                public long foobar() {
                    return System.currentTimeMillis();
                }
            };
            d1.setServerObject(server_object);
            d2.setServerObject(server_object);

            rsps=d2.callRemoteMethods(null, "foobar", null, null, new RequestOptions(ResponseMode.GET_ALL, 5000));
            System.out.println("rsps:\n" + rsps);
            assert rsps.size() == 2;
            for(Rsp rsp: rsps.values()) {
                assert rsp.wasReceived();
                assert !rsp.wasSuspected();
                assert rsp.getValue() != null;
            }
        }
        finally {
            d2.stop();
            d1.stop();
            Util.close(channel2, channel1);
        }
    }


    public void testException() throws Exception {
        RspList<Object> rsps=disp1.callRemoteMethods(null, "throwException", null, null, new RequestOptions(ResponseMode.GET_ALL, 5000));
        for(Rsp<Object> rsp: rsps.values()) {
            System.out.println(rsp);
        }
        for(Rsp<Object> rsp: rsps.values()) {
            assert rsp.getException() != null && rsp.getValue() == null;
        }
    }


    public void testExceptionAsReturnValue() throws Exception {
        RspList<Object> rsps=disp1.callRemoteMethods(null, "returnException", null, null, new RequestOptions(ResponseMode.GET_ALL, 5000));
        for(Rsp<Object> rsp: rsps.values()) {
            System.out.println(rsp);
        }
        for(Rsp<Object> rsp: rsps.values()) {
            assert rsp.getException() == null && rsp.getValue() != null && rsp.getValue() instanceof Throwable;
        }
    }


    public void testUnicastException()  {
        try {
            disp1.callRemoteMethod(c2.getAddress(), "throwException", null, null, new RequestOptions(ResponseMode.GET_ALL, 5000));
        }
        catch(Throwable throwable) {
            System.out.println("received exception (as expected)");
        }
    }

    public void testUnicastExceptionWithFuture()  {
        try {
            MethodCall call=new MethodCall(ServerObject.class.getMethod("throwException"));
            Future<Object> future=disp1.callRemoteMethodWithFuture(c2.getAddress(), call, new RequestOptions(ResponseMode.GET_ALL, 5000));
            Object val=future.get();
            assert val == null;
            assert false : " should not get here";
        }
        catch(Throwable throwable) {
            System.out.println("received exception (as expected): " + throwable);
        }
    }


    public void testUnicastExceptionAsReturnValue() throws Exception {
        Object rsp=disp1.callRemoteMethod(c2.getAddress(), "returnException", null, null, new RequestOptions(ResponseMode.GET_ALL, 5000));
        System.out.println("rsp = " + rsp);
        assert rsp != null && rsp instanceof Throwable;
    }

    public void testUnicastExceptionAsReturnValueWithFuture() throws Exception {
        MethodCall call=new MethodCall(ServerObject.class.getMethod("returnException"));
        Future<Object> future=disp1.callRemoteMethodWithFuture(c2.getAddress(), call, new RequestOptions(ResponseMode.GET_ALL, 5000));
        Object val=future.get();
        assert val instanceof Exception;
    }


    /**
     * Test the response filter mechanism which can be used to filter responses received with
     * a call to RpcDispatcher.
     * 
     * The test filters requests based on the id of the server object they were received
     * from, and only accept responses from servers with id > 1. 
     * 
     * The expected behaviour is that the response from server 1 is rejected, but the responses 
     * from servers 2 and 3 are accepted.
     *
     */
    public void testResponseFilter() throws Exception {
        RequestOptions options=new RequestOptions(ResponseMode.GET_ALL, 10000, false,
                                                  new RspFilter() {
                                                      int num=0;
                                                      public boolean isAcceptable(Object response, Address sender) {
                                                          boolean retval=((Integer)response).intValue() > 1;
                                                          if(retval)
                                                              num++;
                                                          return retval;
                                                      }

                                                      public boolean needMoreResponses() {
                                                          return num < 2;
                                                      }
                                                  });
    	
        RspList rsps=disp1.callRemoteMethods(null, "foo", null, null, options);
        System.out.println("responses are:\n" + rsps);
        assertEquals("there should be three response values", 3, rsps.size());
        assertEquals("number of responses received should be 2", 2, rsps.numReceived());
    }

    /**
     * Tests an incorrect response filter which always returns false for isAcceptable() and true for needsMoreResponses().
     * The call should return anyway after having received all responses, even if none of them was accepted by the
     * filter.
     */
    public void testNonTerminatingResponseFilter() throws Exception {
        RequestOptions options=new RequestOptions(ResponseMode.GET_ALL, 10000, false,
                                                  new RspFilter() {
                                                      public boolean isAcceptable(Object response, Address sender) {
                                                          return false;
                                                      }
                                                      public boolean needMoreResponses() {return true;}
                                                  });

        RspList rsps=disp1.callRemoteMethods(null, "foo", null, null, options);
        System.out.println("responses are:\n" + rsps);
        assertEquals("there should be three response values", 3, rsps.size());
        assertEquals("number of responses received should be 3", 0, rsps.numReceived());
    }

    /**
     * Runs with response mode of GET_FIRST and the response filter accepts only the last response
     * @throws Exception
     */
    public void testAcceptLastResponseFilter() throws Exception {
        RequestOptions options=new RequestOptions(ResponseMode.GET_FIRST, 10000, false,
                                                  new RspFilter() {
                                                      int count=0;
                                                      public boolean isAcceptable(Object response, Address sender) {
                                                          return ++count >= 3;
                                                      }
                                                      public boolean needMoreResponses() {return count < 3;}
                                                  });

        RspList rsps=disp1.callRemoteMethods(null, "foo", null, null, options);
        System.out.println("responses are:\n" + rsps);
        assertEquals("there should be three response values", 3, rsps.size());
        assertEquals("number of responses received should be 3", 1, rsps.numReceived());
    }


    public void testFuture() throws Exception {
        MethodCall sleep=new MethodCall("sleep", new Object[]{1000L}, new Class[]{long.class});
        Future<RspList<Object>> future=disp1.callRemoteMethodsWithFuture(null, sleep, new RequestOptions(ResponseMode.GET_ALL, 5000L, false, null));
        assert !future.isDone();
        assert !future.isCancelled();
        try {
            future.get(300, TimeUnit.MILLISECONDS);
            assert false : "we should not get here, get(300) should have thrown a TimeoutException";
        }
        catch(TimeoutException e) {
            System.out.println("got TimeoutException - as expected");
        }
        
        assert !future.isDone();

        RspList result=future.get(6000L, TimeUnit.MILLISECONDS);
        System.out.println("result:\n" + result);
        assert result != null;
        assert result.size() == 3;
        assert future.isDone();
    }


    public void testNotifyingFuture() throws Exception {
        MethodCall sleep=new MethodCall("sleep", new Object[]{1000L}, new Class[]{long.class});
        MyFutureListener<RspList<Long>> listener=new MyFutureListener<RspList<Long>>();
        NotifyingFuture<RspList<Long>> future=disp1.callRemoteMethodsWithFuture(null, sleep, new RequestOptions(ResponseMode.GET_ALL, 5000L, false, null));
        future.setListener(listener);
        assert !future.isDone();
        assert !future.isCancelled();
        assert !listener.isDone();
        Util.sleep(2000);
        assert listener.isDone();
        RspList<Long> result=future.get(1L, TimeUnit.MILLISECONDS);
        System.out.println("result:\n" + result);
        assert result != null;
        assert result.size() == 3;
        assert future.isDone();
    }

    public void testNotifyingFutureWithDelayedListener() throws Exception {
        MethodCall sleep=new MethodCall("sleep", new Object[]{1000L}, new Class[]{long.class});
        NotifyingFuture<RspList<Long>> future;
        MyFutureListener<RspList<Long>> listener=new MyFutureListener<RspList<Long>>();
        future=disp1.callRemoteMethodsWithFuture(null, sleep, new RequestOptions(ResponseMode.GET_ALL, 5000L, false, null));
        assert !future.isDone();
        assert !future.isCancelled();

        Util.sleep(2000);
        future.setListener(listener);
        assert listener.isDone();
        RspList result=future.get(1L, TimeUnit.MILLISECONDS);
        System.out.println("result:\n" + result);
        assert result != null;
        assert result.size() == 3;
        assert future.isDone();
    }


    public void testMultipleFutures() throws Exception {
        MethodCall sleep=new MethodCall("sleep", new Object[]{100L}, new Class[]{long.class});
        List<Future<RspList<Long>>> futures=new ArrayList<Future<RspList<Long>>>();
        long target=System.currentTimeMillis() + 30000L;

        Future<RspList<Long>> future;
        RequestOptions options=new RequestOptions(ResponseMode.GET_ALL, 30000L, false, null);
        for(int i=0; i < 10; i++) {
            future=disp1.callRemoteMethodsWithFuture(null, sleep, options);
            futures.add(future);
        }

        List<Future<RspList<Long>>> rsps=new ArrayList<Future<RspList<Long>>>();
        while(!futures.isEmpty() && System.currentTimeMillis() < target) {
            for(Iterator<Future<RspList<Long>>> it=futures.iterator(); it.hasNext();) {
                future=it.next();
                if(future.isDone()) {
                    it.remove();
                    rsps.add(future);
                }
            }
            System.out.println("pending responses: " + futures.size());
            Util.sleep(200);
        }
        System.out.println("\n" + rsps.size() + " responses:\n");
        for(Future<RspList<Long>> tmp: rsps) {
            System.out.println(tmp);
        }
    }

    public void testMultipleNotifyingFutures() throws Exception {
        MethodCall sleep=new MethodCall("sleep", new Object[]{100L}, new Class[]{long.class});
        List<MyFutureListener<RspList<Long>>> listeners=new ArrayList<MyFutureListener<RspList<Long>>>();
        RequestOptions options=new RequestOptions(ResponseMode.GET_ALL, 30000L, false, null);
        for(int i=0; i < 10; i++) {
            MyFutureListener<RspList<Long>> listener=new MyFutureListener<RspList<Long>>();
            listeners.add(listener);
            // NotifyingFuture<RspList<Long>> futures=disp1.callRemoteMethodsWithFuture(null, sleep, options);
            NotifyingFuture<RspList<Long>> futures=disp1.callRemoteMethodsWithFuture(null, sleep, options);
            futures.setListener(listener);
        }

        Util.sleep(1000);
        for(int i=0; i < 10; i++) {
            boolean all_done=true;
            for(MyFutureListener<RspList<Long>> listener: listeners) {
                boolean done=listener.isDone();
                System.out.print(done? "+ " : "- ");
                if(!listener.isDone())
                    all_done=false;
            }
            if(all_done)
                break;
            Util.sleep(500);
            System.out.println("");
        }
        
        for(MyFutureListener listener: listeners) {
            assert listener.isDone();
        }

    }




    public void testFutureCancel() throws Exception {
        MethodCall sleep=new MethodCall("sleep", new Object[]{1000L}, new Class[]{long.class});
        NotifyingFuture<RspList<Long>> future=disp1.callRemoteMethodsWithFuture(null, sleep, new RequestOptions(ResponseMode.GET_ALL, 5000L));
        assert !future.isDone();
        assert !future.isCancelled();
        future.cancel(true);
        assert future.isDone();
        assert future.isCancelled();

        future=disp1.callRemoteMethodsWithFuture(null, sleep, new RequestOptions(ResponseMode.GET_ALL, 0));
        assert !future.isDone();
        assert !future.isCancelled();
        future.cancel(true);
        assert future.isDone();
        assert future.isCancelled();
    }


    /**
     * Test the ability of RpcDispatcher to handle large argument and return values
     * with multicast RPC calls.
     * 
     * The test sends requests for return values (byte arrays) having increasing sizes,
     * which increase the processing time for requests as well as the amount of memory
     * required to process requests.
     * 
     * The expected behaviour is that all RPC requests complete successfully.
     *
     */
    public void testLargeReturnValue() throws Exception {
        setProps(c1, c2, c3);
        for(int i=0; i < SIZES.length; i++) {
            _testLargeValue(SIZES[i]);
        }
    }
    
    /**
     * Test the ability of RpcDispatcher to handle huge argument and return values
     * with multicast RPC calls.
     * 
     * The test sends requests for return values (byte arrays) having increasing sizes,
     * which increase the processing time for requests as well as the amount of memory
     * required to process requests.
     * 
     * The expected behaviour is that RPC requests either timeout or trigger out of 
     * memory exceptions. Huge return values extend the processing time required; but
     * the length of time depends upon the speed of the machine the test runs on. 
     *
     */
    /*@Test(groups="first")
    public void testHugeReturnValue() {
        setProps(c1, c2, c3);
        for(int i=0; i < HUGESIZES.length; i++) {
            _testHugeValue(HUGESIZES[i]);
        }
    }*/
    

    /**
     * Tests a method call to {A,B,C} where C left *before* the call. http://jira.jboss.com/jira/browse/JGRP-620
     */
    public void testMethodInvocationToNonExistingMembers() throws Exception {
    	
    	final int timeout = 5 * 1000 ;
    	
    	// get the current membership, as seen by C
        View view=c3.getView();
        List<Address> members=view.getMembers();
        System.out.println("list is " + members);

        // cause C to leave the group and close its channel
        System.out.println("closing c3");
        c3.close();

        Util.sleep(1000);
        
        // make an RPC call using C's now outdated view of membership
        System.out.println("calling method foo() in " + members + " (view=" + c2.getView() + ")");
        RspList<Object> rsps=disp1.callRemoteMethods(members, "foo", null, null, new RequestOptions(ResponseMode.GET_ALL, timeout));
        
        // all responses 
        System.out.println("responses:\n" + rsps);
        for(Map.Entry<Address,Rsp<Object>> entry: rsps.entrySet()) {
            Rsp rsp=entry.getValue();
            assertTrue("response from " + entry.getKey() + " was not received", rsp.wasReceived());
            assertFalse(rsp.wasSuspected());
        }
    }


    /**
     * Test the ability of RpcDispatcher to handle large argument and return values
     * with unicast RPC calls.
     * 
     * The test sends requests for return values (byte arrays) having increasing sizes,
     * which increase the processing time for requests as well as the amount of memory
     * required to process requests.
     * 
     * The expected behaviour is that all RPC requests complete successfully.
     *
     */
    public void testLargeReturnValueUnicastCall() throws Exception {
        setProps(c1, c2, c3);
        for(int i=0; i < SIZES.length; i++) {
            _testLargeValueUnicastCall(c1.getAddress(), SIZES[i]);
        }
    }


    private static void setProps(JChannel... channels) {
        for(JChannel ch: channels) {
            Protocol prot=ch.getProtocolStack().findProtocol("FRAG2");
            if(prot != null) {
                ((FRAG2)prot).setFragSize(12000);
            }
            prot=ch.getProtocolStack().findProtocol("FRAG");
            if(prot != null) {
                ((FRAG)prot).setFragSize(12000);
            }

            prot=ch.getProtocolStack().getTransport();
            if(prot != null)
                ((TP)prot).setMaxBundleSize(14000);
        }
    }

    /**
     * Helper method to perform a RPC call on server method "returnValue(int size)" for 
     * all group members.
     * 
     * The method checks that each returned value is non-null and has the correct size. 
     *    
     */
    void _testLargeValue(int size) throws Exception {
    	
    	// 20 second timeout 
    	final long timeout = 20 * 1000 ;
    		
        System.out.println("\ntesting with " + size + " bytes");
        RspList<Object> rsps=disp1.callRemoteMethods(null, "largeReturnValue", new Object[]{size}, new Class[]{int.class},
                                             new RequestOptions(ResponseMode.GET_ALL, timeout));
        System.out.println("rsps:");
        assert rsps.size() == 3 : "there should be three responses to the RPC call but only " + rsps.size() +
                " were received: " + rsps;
        
        for(Map.Entry<Address,Rsp<Object>> entry: rsps.entrySet()) {
        	
        	// its possible that an exception was raised in processing
        	Object obj = entry.getValue().getValue() ;
        	
        	// this should not happen
        	assert !(obj instanceof Throwable) : "exception was raised in processing reasonably sized argument";
        	
            byte[] val=(byte[]) obj;
            assert val != null;
            System.out.println(val.length + " bytes from " + entry.getValue().getSender());
            assert val.length == size : "return value does not match required size";
        }
    }
    
    /**
     * Helper method to perform a RPC call on server method "returnValue(int size)" for 
     * all group members.
     * 
     * This method need to take into account that RPC calls can timeout with huge values,
     * and they can also trigger OOMEs. But if we are lucky, they can also return
     * reasonable values. 
     * 
     */
    void _testHugeValue(int size) throws Exception {
    	
    	// 20 second timeout 
    	final long timeout = 20 * 1000 ;
    	
        System.out.println("\ntesting with " + size + " bytes");
        RspList<Object> rsps=disp1.callRemoteMethods(null, "largeReturnValue", new Object[]{size}, new Class[]{int.class},
                                                     new RequestOptions(ResponseMode.GET_ALL, timeout));
        System.out.println("rsps:");
        assert rsps != null;
        assert rsps.size() == 3 : "there should be three responses to the RPC call but only " + rsps.size() +
                " were received: " + rsps;

        // in checking the return values, we need to take account of timeouts (i.e. when
        // a null value is returned) and exceptions 
        for(Map.Entry<Address,Rsp<Object>> entry: rsps.entrySet()) {

        	Object obj = entry.getValue().getValue() ;

        	// its possible that an exception was raised
        	if (obj instanceof java.lang.Throwable) {
        		Throwable t = (Throwable) obj ;
        		
        		System.out.println(t.toString() + " exception was raised processing argument from " +
        							entry.getValue().getSender() + " -this is expected") ;
        		continue ;
        	}        	
        	
        	// its possible that the request timed out before the serve could reply 
        	if (obj == null) {
        		System.out.println("request timed out processing argument from " + 
        							entry.getValue().getSender() + " - this is expected") ;
        		continue ;       	
        	}
        	
        	// if we reach here, we sould have a reasobable value
        	byte[] val=(byte[]) obj;
            System.out.println(val.length + " bytes from " + entry.getValue().getSender());
            assert val.length == size : "return value does not match required size";
        }
    }

    /**
     * Helper method to perform a RPC call on server method "returnValue(int size)" for 
     * an individual group member. 
     * 
     * The method checks that the returned value is non-null and has the correct size. 
     * 
     * @param dst the group member
     * @param size the size of the byte array to be returned
     */
    void _testLargeValueUnicastCall(Address dst, int size) throws Exception {
    	
    	// 20 second timeout
    	final long timeout = 20 * 1000 ;
    	
        System.out.println("\ntesting unicast call with " + size + " bytes");
        assertNotNull(dst);

        byte[] val=disp1.callRemoteMethod(dst, "largeReturnValue", new Object[]{size}, new Class[]{int.class},
                                             new RequestOptions(ResponseMode.GET_ALL, timeout));
        
        // check value is not null, otherwise fail the test
        assertNotNull("return value should be non-null", val);
        System.out.println("rsp: " + val.length + " bytes");
        
        // returned value should have requested size
        assertEquals("return value does not match requested size", size, val.length);
    }

    /**
     * This class serves as a server obect to turn requests into replies.
     * It is initialised with an integer id value.
     * 
     * It implements two functions:
     * function foo() returns the id of the server
     * function largeReturnValue(int size) returns a byte array of size 'size'
     *  
     */
    private static class ServerObject {
        int i;
        public ServerObject(int i) {
            this.i=i;
        }
        public int foo() {return i;}
        
        public static long sleep(long timeout) {
            // System.out.println("sleep()");
            long start=System.currentTimeMillis();
            Util.sleep(timeout);
            //throw new NullPointerException("boom");
            return System.currentTimeMillis() - start;
        }


        public static void throwException() throws Exception {
            throw new Exception("booom");
        }

        public static Exception returnException() {
            return new Exception("booom");
        }


        public static byte[] largeReturnValue(int size) {
            return new byte[size];
        }
    }

    private static class MyFutureListener<T> implements FutureListener<T> {
        private boolean done;

        public void futureDone(Future<T> future) {
            done=true;
        }

        public boolean isDone() {return done;}
    }


}