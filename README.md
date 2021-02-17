# BearMaps
A web mapping application project from CS 61B, UC Berkeley.

I built the "smart" pieces of the back end of this application.

Please be generous with the free server and [Bear Maps!](http://bearmaps-rui-gao.herokuapp.com/map.html)
![image](https://user-images.githubusercontent.com/48979946/108189048-a63a6080-7153-11eb-9636-d23f665e724b.png)

# Features
## View
### Region
The map data covers a total region between longitudes -122.29980468 and -122.21191406 and between latitudes 37.82280243 and 37.89219554.

### Move Around
Always displays the correct map during moving around and resizing the web browser window.

### Zoom In/Out
Meets users' needs with up to 8 levels of resolutions.

![image](https://user-images.githubusercontent.com/48979946/108193575-f5cf5b00-7158-11eb-8e45-976c5eafaa8d.png)![image](https://user-images.githubusercontent.com/48979946/108193692-139cc000-7159-11eb-8ee0-58f1e7645cab.png)

## Routing
Indicate the start and destination points just by double clicking on the map, the shortest route will be provided.

![image](https://user-images.githubusercontent.com/48979946/108193263-9a04d200-7158-11eb-91c1-e0b59fe9dca2.png)

## Search
Find any location just by typing its name in the lovely box:

![image](https://user-images.githubusercontent.com/48979946/108195421-139dbf80-715b-11eb-971a-9b6625bab3e0.png)

e.g. would you like some Top Dog?
![image](https://user-images.githubusercontent.com/48979946/108195598-5495d400-715b-11eb-8977-9f888646c242.png)
### Autocomplete
Doesn't remember the full name, or just feels bad to type so much?

We got autocomplete here! This smart feature will react immedieately during your typing, and gives you a list of place names you might want in most searched order.

e.g. To find "Model Shoe Renew"

![image](https://user-images.githubusercontent.com/48979946/108197058-34671480-715d-11eb-94e7-4ce9ae5c6fea.png)

Oh, not here :(  One more letter

![image](https://user-images.githubusercontent.com/48979946/108197315-8f990700-715d-11eb-82b6-be5553189f65.png)

Got it!

![image](https://user-images.githubusercontent.com/48979946/108197098-4052d680-715d-11eb-9cfb-f0d57e6dfd6d.png)

Again. Amazing, isn't it?
![image](https://user-images.githubusercontent.com/48979946/108197127-4b0d6b80-715d-11eb-8a6b-e58669d02e70.png)

# Implementation
I implemented the back end in Java.
## Map Rastering
### Algorithm
Returns images that:
- Include any region of the query box.
- Have the greatest longitudinal distance per pixel (LonDPP) that is less than or equal to the LonDPP of the query box (as zoomed out as possible). If the requested LonDPP is less than what is available in the data files, use the lowest LonDPP available instead (i.e. depth 7 images).
  
![image](https://fa20.datastructur.es/materials/proj/proj2d/rastering_example.png)

### Runtime
O(k), where k is number of tiles intersecting a query box.

## Closest Place
For some point on the map, whose longitude and latitude are given, find the place that is closest to this point. This will be used for routing.
### Algorithm
Uses a date structure known as a k-d tree, with optimization for nearest search by pruning "bad" sides.
### Runtime
O(log n) on average, where n is the total number of places.

## Routing
### Algorithm
Memory-Optimizing A* Graph Search with great-circle distance as heuristic.
### Runtime
Much faster than O(E log V) of Dijkstra's, where E is the total number of edges and V is the number of vertices.

## Autocomplete
### Algorithm
Uses a data structure known as a trie with several optimizations for much more efficient and smart prefix searching used by autocomplete.
### Runtime
O(k) where k is the number of words sharing the query prefix.

# Acknowledgements
Cite from [project page](https://fa20.datastructur.es/materials/proj/proj2d/proj2d)

This project was originally created by [Alan Yao](https://www.linkedin.com/in/alanyao). The tile images and map feature data for this project was downloaded from [OpenStreetMap](http://www.openstreetmap.org/) project.

Data made available by OSM under the Open Database License. JavaSpark web framework and Google Gson library.

Alan Yao for creating the original version of this project. Colby Yuan and Eli Lipsitz for substantial refactoring of the front end. Rahul Nangia for substantial refactoring of the back end.