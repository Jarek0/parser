package pl.edu.pollub.parser.domain.assertions

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node

class XmlAssert {

    private boolean nodeTypeDiff = true
    private boolean nodeValueDiff = true

    boolean diff(String actual, String expected, List<String> diffs = new ArrayList<>()) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
        dbf.setNamespaceAware(true)
        dbf.setCoalescing(true)
        dbf.setIgnoringElementContentWhitespace(true)
        dbf.setIgnoringComments(true)
        DocumentBuilder db = dbf.newDocumentBuilder()


        Document doc1 = db.parse(new ByteArrayInputStream(actual.getBytes()))
        Document doc2 = db.parse(new ByteArrayInputStream(expected.getBytes()))

        doc1.normalizeDocument()
        doc2.normalizeDocument()

        return diff(doc1, doc2, diffs)
    }

    boolean diff(Node node1, Node node2, List<String> diffs) throws Exception {
        
        if(diffNodeExists(node1, node2, diffs)) {
            return true
        }

        if(nodeTypeDiff) {
            diffNodeType(node1, node2, diffs)
        }

        if(nodeValueDiff) {
            diffNodeValue(node1, node2, diffs)
        }
        
        diffAttributes(node1, node2, diffs)
        diffNodes(node1, node2, diffs)

        return diffs.size() > 0
    }

    boolean diffNodes(Node node1, Node node2, List<String> diffs) throws Exception {

        Map<String,Node> children1 = new LinkedHashMap<String,Node>()
        for(Node child1 = node1.getFirstChild(); child1 != null; child1 = child1.getNextSibling()) {
            children1.put(child1.getNodeName(), child1)
        }


        Map<String,Node> children2 = new LinkedHashMap<String,Node>()
        for(Node child2 = node2.getFirstChild(); child2 != null; child2 = child2.getNextSibling()) {
            children2.put(child2.getNodeName(), child2)
        }


        for(Node child1 : children1.values()) {
            Node child2 = children2.remove(child1.getNodeName())
            diff(child1, child2, diffs)
        }

        for(Node child2 : children2.values()) {
            Node child1 = children1.get(child2.getNodeName())
            diff(child1, child2, diffs)
        }

        return diffs.size() > 0
    }

    boolean diffAttributes(Node node1, Node node2, List<String> diffs) throws Exception
    {
        NamedNodeMap nodeMap1 = node1.getAttributes()
        Map<String,Node> attributes1 = new LinkedHashMap<String,Node>()
        for(int index = 0; nodeMap1 != null && index < nodeMap1.getLength(); index++) {
            attributes1.put(nodeMap1.item(index).getNodeName(), nodeMap1.item(index))
        }

        NamedNodeMap nodeMap2 = node2.getAttributes()
        Map<String,Node> attributes2 = new LinkedHashMap<String,Node>()
        for(int index = 0; nodeMap2 != null && index < nodeMap2.getLength(); index++) {
            attributes2.put(nodeMap2.item(index).getNodeName(), nodeMap2.item(index))

        }

        for(Node attribute1 : attributes1.values()) {
            Node attribute2 = attributes2.remove(attribute1.getNodeName())
            diff(attribute1, attribute2, diffs)
        }

        for(Node attribute2 : attributes2.values()) {
            Node attribute1 = attributes1.get(attribute2.getNodeName())
            diff(attribute1, attribute2, diffs)
        }

        return diffs.size() > 0
    }

    static boolean diffNodeExists(Node node1, Node node2, List<String> diffs) throws Exception {
        if(node1 == null && node2 == null) {
            diffs.add(getPath(node2) + ":node " + node1 + "!=" + node2 + "\n")
            return true
        }

        if(node1 == null && node2 != null) {
            diffs.add(getPath(node2) + ":node " + node1 + "!=" + node2.getNodeName())
            return true
        }

        if(node1 != null && node2 == null) {
            diffs.add(getPath(node1) + ":node " + node1.getNodeName() + "!=" + node2)
            return true
        }

        return false
    }

    static boolean diffNodeType(Node node1, Node node2, List<String> diffs) throws Exception {
        if(node1.getNodeType() != node2.getNodeType()) {
            diffs.add(getPath(node1) + ":type " + node1.getNodeType() + "!=" + node2.getNodeType())
            return true
        }

        return false
    }

    static boolean diffNodeValue(Node node1, Node node2, List<String> diffs) throws Exception {
        if(node1.getNodeValue() == null && node2.getNodeValue() == null) {
            return false
        }

        if(node1.getNodeValue() == null && node2.getNodeValue() != null) {
            diffs.add(getPath(node1) + ":type " + node1 + "!=" + node2.getNodeValue())
            return true
        }

        if(node1.getNodeValue() != null && node2.getNodeValue() == null) {
            diffs.add(getPath(node1) + ":type " + node1.getNodeValue() + "!=" + node2)
            return true
        }

        if(node1.getNodeValue().replaceAll("\\s","") != node2.getNodeValue().replaceAll("\\s","")) {
            diffs.add(getPath(node1) + ":type " + node1.getNodeValue() + "!=" + node2.getNodeValue())
            return true
        }

        return false
    }

    static String getPath(Node node) {
        StringBuilder path = new StringBuilder()

        path.insert(0, node.getNodeName())
        path.insert(0, "/")
        while((node = node.getParentNode()) != null){
            path.insert(0, node.getNodeName())
            path.insert(0, "/")
        }
            
        return path.toString()
    }
}
