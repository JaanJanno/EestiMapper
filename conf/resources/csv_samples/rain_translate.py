data = [e for e in open("result.csv", encoding='utf-8')]
data = data[1:]

file = open('out.csv','w', encoding='utf-8')
for e in data:
	n, v, y = e.split(',')
	n = n.lower()
	n = n.replace('rural municipality','vald')
	n = n.replace('county','maakond')
	n = n.replace('city','linn')
	print(','.join([n, v, y]), end='')
	file.write(','.join([n, v, y]))

file.close()