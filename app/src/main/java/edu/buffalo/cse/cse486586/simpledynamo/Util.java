package edu.buffalo.cse.cse486586.simpledynamo;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;


public class Util {
    static final String TAG = Util.class.getSimpleName();
    public static String genHash(String input) throws NoSuchAlgorithmException {

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] sha1Hash = sha1.digest(input.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : sha1Hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    //return true if the key can be found on the passed node.
    public static boolean lookup(String keyHash, NodeDetails node){
        //Log.d(TAG, "***********************lookup begins************************");
        //Log.d(TAG,"====================================");
        //Log.d(TAG,"KeyHash : " + keyHash + " Node details : " + node.toString());
        //Log.d(TAG,"====================================");
        boolean retVal = false;
        if(node.isFirstNode()){
            if (keyHash.compareTo(node.getPredecessorNodeIdHash()) > 0 ||
                    keyHash.compareTo(node.getNodeIdHash()) <= 0) {
                retVal = true;
            } else
                retVal = false;

        }else {
            if (keyHash.compareTo(node.getPredecessorNodeIdHash()) > 0 &&
                    keyHash.compareTo(node.getNodeIdHash()) <= 0) {
                retVal = true;
            } else
                retVal = false;
        }
        //Log.d(TAG, "***********************lookup ends************************");
        return retVal;
    }

    public static String[] getReplicationPortNumbers(NodeDetails targetNode){
        Log.d(TAG, "***********************getReplicationPortNumbers begins************************");
        String[] replicationPorts = new String[3];
        NodeDetails nextNode = null;

        String port1 = "";
        String port2 = "";

        for (NodeDetails node :SimpleDynamoProvider.chordNodeList) {
            if(node.getPort().equals(targetNode.getPort())){
                port1 = node.getSuccessorPort();
                replicationPorts[1] = port1;
                break;
            }
        }

        for (NodeDetails node :SimpleDynamoProvider.chordNodeList) {
            if(node.getPort().equals(port1)) {
                port2 = node.getSuccessorPort();
                replicationPorts[2] = port2;
                break;
            }
        }

        Log.d(TAG,"====================================");
        Log.e(TAG,"Port number of targetNode : " + targetNode.getPort());
        Log.e(TAG,"Replication port are : " + replicationPorts[1] + " and "+ replicationPorts[2]);
        Log.d(TAG,"====================================");

        Log.d(TAG, "***********************getReplicationPortNumbers ends************************");
        return replicationPorts;
    }

    public static NodeDetails getTargetNode(String keyHash){
        Log.d(TAG, "***********************getTargetNode begins************************");
        NodeDetails targetNode = null;
        for (NodeDetails node :SimpleDynamoProvider.chordNodeList) {
            if(Util.lookup(keyHash, node)){
                targetNode = node;
                break;
            }
        }
        Log.d(TAG,"====================================");
        Log.d(TAG,"KeyHash : " + keyHash + " Target Node details : " + targetNode.toString());
        Log.d(TAG,"====================================");
        Log.d(TAG, "***********************getTargetNode ends************************");
        return targetNode;
    }


}
