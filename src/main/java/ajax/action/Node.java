package ajax.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Node {
	
	private String name;
	private String content;
	
	private List<Node> nodes = new ArrayList<>();
	
	public Node(String inName) {
		this(inName, (Node) null);
	}
	
	public Node(String inName, String inContent) {
		name = inName;
		content = inContent;
	}
	
	public Node(String inName, Node inNode) {
		name = inName;
		addNode(inNode);
		
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Node> getNodes() {
		return nodes;
	}
	
	public void addNode(Node inNode) {
		if (Objects.nonNull(inNode)) {
			nodes.add(inNode);
		}
	}
	
	public String toString() {
		StringBuilder response = new StringBuilder();

		response.append("<" + name + ">");
		
		if (Objects.nonNull(content)) {
			response.append(content);
		}
		
		if (nodes.size() > 0) {
			List<String> nodesContents = nodes.stream()
					.map(Node::toString)
					.collect(Collectors.toList());
			
			response.append(String.join("", nodesContents));
		}
		
		response.append("</" + name + ">");
		
		return response.toString();
	}

}
