package utils

import "errors"

type NamespaceGraphNode struct {
	RelationName string
	Children     []*NamespaceGraphNode
}

type NamespaceGraph struct {
	Nodes map[string]*NamespaceGraphNode
}

func (graph *NamespaceGraph) InsertNode(relation string) {
	if _, exists := graph.Nodes[relation]; !exists {
		graph.Nodes[relation] = &NamespaceGraphNode{RelationName: relation, Children: []*NamespaceGraphNode{}}
	}
}

func (graph *NamespaceGraph) InsertConnection(parentRelation string, childRelation string) error {
	nodeParent, existsParent := graph.Nodes[parentRelation]
	nodeChild, existsChild := graph.Nodes[childRelation]
	if !existsParent || !existsChild {
		return errors.New("graph doesn't contain both relations")
	}
	nodeParent.Children = append(nodeParent.Children, nodeChild)
	return nil
}
func NewNamespaceGraph() NamespaceGraph {
	return NamespaceGraph{Nodes: make(map[string]*NamespaceGraphNode)}
}
