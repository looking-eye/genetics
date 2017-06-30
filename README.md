# genetics
A genetic algorithm, implemented in Java that tries to learn how to draw a given picture, by putting colored circles on a canvas. It does that by calculating the squared color distance between a given (user specified) prototype picture and the phenotype (generated picture) of the evolved image.

The population for this algorithm consists of a (configurable) number of individuals. Each individual is specified by a genome (genotype), which consists of a number of circles (features). Each circle has a diameter and three color channels (R, G, B). These circles are drawn on a canvas and form the phenotype. You can specify the mutation probability, which is evaluated for each attribute of every circle seperately, as well as the maximum mutation (e.g. 20px for the max delte between old and new circle diameter). 

After every generation the three(?) best individuals are cloned into the next generation. The rest of the population is filled with crossovers between random parents. You can either choose to kill the three average individuals, or the worst three (removeWorst=true), to account for the three best that were cloned. (Otherwise, your population would grow).

The number of circles per genome are calculated, based on the input picture: x * y / fractionCircleCount. You should balance this with your maxDiameter for your circles.


# Startup
    java -jar genetics.jar yourPrototypePic.png
  
  
