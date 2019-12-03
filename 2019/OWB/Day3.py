def readInput():
	filepath = 'Day3.txt'
	print()
	wire1 = []
	wire2 = []
	with open(filepath) as fp:
		wire1 = fp.readline().strip().split(',')
		wire2 = fp.readline().strip().split(',')

	return (wire1, wire2)

def distance(x1, y1, x2, y2):
	return abs(x2-x1)+abs(y2-y1)

def trackWire(wire):
	coords = []
	(x,y) = (0, 0)
	for step in wire:
		(cx, cy) = (0,0)
		dir = step[0:1]
		if dir == 'U':
			(cx, cy) = (1, 0)
		if dir == 'D':
			(cx, cy) = (-1, 0)
		if dir == 'L':
			(cx, cy) = (0, -1)
		if dir == 'R':
			(cx, cy) = (0, 1)
		
		for i in range(int(step[1:])):
			x += cx
			y += cy
			coords.append((x, y))
	#print(coords)
	return coords

def puzzle1():
	(w1, w2) = readInput()
	coords1 = trackWire(w1)
	coords2 = trackWire(w2)
	overlaps = [v for v in coords1 if v in coords2]
	dist = [distance(x, y, 0, 0) for (x, y) in overlaps]
	dist.sort()
	print(overlaps)
	print(dist)
	return dist[0]
	

print(puzzle1())
