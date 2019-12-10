def readInput():
    filepath = 'Day6.txt'
    edges = []
    with open(filepath) as fp:
        line = fp.readline()
        while (line):
            n = line.strip().split(')')
            edges.append((n[0], n[1]))
            line = fp.readline()
    return edges
    
def buildGraph(edges):
    graph = {}
    startNode = next((x for x in edges if x[0] == 'COM'), None)
    addEdgeToGraph(startNode, 0, edges, graph)
    return graph

def addEdgeToGraph(edge, distance, edges, graph):
    if edge[0] not in graph:
        graph[edge[0]] = []
    graph[edge[0]].append((edge[1], distance+1))
    
    for e in [x for x in edges if x[0] == edge[1]]:
        addEdgeToGraph(e, distance + 1, edges, graph)
        
def search(edgeFrom, edgeTo, graph):
    searchList = [edgeFrom]
    visited = [edgeFrom]
    while len(searchList) > 0:
        
    

def puzzle1():
    edges = readInput()
    graph = buildGraph(edges)
    sum = 0
    for k in graph:
        for edge in graph[k]:
            sum += edge[1]
    return sum

def puzzle2():
    return

######### PUZZLE 6 #########
print("Day 6:", puzzle1(), puzzle2())
