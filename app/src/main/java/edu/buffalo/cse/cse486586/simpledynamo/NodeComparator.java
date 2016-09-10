package edu.buffalo.cse.cse486586.simpledynamo;

import java.util.Comparator;


public class NodeComparator implements Comparator<NodeDetails> {
    @Override
    public int compare(NodeDetails n1, NodeDetails n2){

        return n1.getNodeIdHash().compareTo(n2.getNodeIdHash());
    }
}
