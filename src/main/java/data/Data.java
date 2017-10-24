package data;

import java.io.IOException;
import java.util.Arrays;

/**
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */

public class Data {
    
    private final int size;
    private int dimension;
    private String name, type, comment;
    private String edgeWeightType, edgeWeightFormat, displayDataTime;
    private final float[] nodeCoordX, nodeCoordY;
    private final int[] currentTour;
    private final int[] optimalTour;
    private final float[][] adjacencyMatrix;

    
    public Data(int size) {
        this.size = size;
        this.nodeCoordX = new float[this.size + 1];
        this.nodeCoordY = new float[this.size + 1];
        this.adjacencyMatrix = new float[this.size + 1][this.size + 1];
        this.currentTour = new int[this.size + 1];
        this.optimalTour = new int[this.size + 1];
    }
    
    public static Data load(String dataPath) {
        
        Data data = new Data(Integer.parseInt(dataPath.replaceAll("\\D+","")));
        try {
            DataParser.parse(dataPath + ".tsp", data);
            DataParser.parse(dataPath + ".opt.tour", data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    
    public void initData() {
        float x, y, x1, x2, y1, y2;
        for(int i = 1; i <= dimension; ++i) {
            for(int j = i + 1; j <= dimension; ++j) {
                x1 = getNodeCoordX(i);
                y1 = getNodeCoordY(i);
                x2 = getNodeCoordX(j);
                y2 = getNodeCoordY(j);
                x = x2 - x1;
                y = y2 - y1;
                this.adjacencyMatrix[i][j] = this.adjacencyMatrix[j][i] = (float) Math.sqrt(x * x + y * y);
            }
        }
    }
    
    public void printData() { 
        for(int i = 1; i <= dimension; ++i) {
            for(int j = 1; j <= dimension; ++j) {
                System.out.print(getMatrixValue(i, j) + " ");
            }
            System.out.println();
        }
    }
     
    public void printCurrentTour() { 
        for(int i = 0; i < dimension; ++i) {
            System.out.print(getCurrentTour(i) + " ");
        }
        System.out.println("");
    }
    
    public void printCoord() { 
        for(int i = 1; i <= dimension; ++i) {
            System.out.println(i + " " + getNodeCoordX(i) + ", " + getNodeCoordY(i));
        }
    }
    
    public void printOptimalTour() { 
        for(int i = 0; i < dimension; ++i) {
            System.out.print(getOptimalTour(i) + " ");
        }
        System.out.println("");
    }
    
    public float evaluate(int[] tour) {
        float eval = 0;
        for(int i = 1; i < dimension; ++i) {
            eval += this.adjacencyMatrix[tour[i - 1]][tour[i]];
        }
        eval += this.adjacencyMatrix[tour[dimension - 1]][tour[0]];
        return eval;
    }
    
    public int getSize() { 
        return size; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getType() { 
        return type; 
    }
    
    public void setType(String type) { 
        this.type = type; 
    }
    
    public String getComment() { 
        return comment; 
    }
    
    public void setComment(String comment) { 
        this.comment = comment; 
    }
    
    public int getDimension() { 
        return dimension; 
    }
    
    public void setDimension(int dimension) { 
        this.dimension = dimension; 
    }
    
    public String getEdgeWeightType() { 
        return edgeWeightType; 
    }
    
    public void setEdgeWeightType(String edgeWeightType) { 
        this.edgeWeightType = edgeWeightType; 
    }
    
    public String getEdgeWeightFormat() { 
        return edgeWeightFormat; 
    }
    
    public void setEdgeWeightFormat(String edgeWeightFormat) { 
        this.edgeWeightFormat = edgeWeightFormat; 
    }
    
    public String getDisplayDataTime() { 
        return displayDataTime; 
    }
    
    public void setDisplayDataTime(String displayDataTime) { 
        this.displayDataTime = displayDataTime; 
    }
    
    public float getNodeCoordX(int i) { 
        return nodeCoordX[i]; 
    }
    
    public void setNodeCoordX(int i, float value) { 
        this.nodeCoordX[i] = value; 
    }
      
    public float getNodeCoordY(int i) { 
        return nodeCoordY[i]; 
    }
    
    public void setNodeCoordY(int i, float value) { 
        this.nodeCoordY[i] = value; 
    }
    
    public void setNodeCoord(int i, float valueX, float valueY) { 
        this.setNodeCoordX(i, valueX);
        this.setNodeCoordY(i, valueY);
    }
    
    public float[][] getAdjacencyMatrix() { 
        return this.adjacencyMatrix; 
    }
    
    public float[] getNeighbors(int i) {
        return this.adjacencyMatrix[i];
    }
    
    public float getMatrixValue(int i, int j) {
        return this.adjacencyMatrix[i][j];
    }
    
    public void setMatrixValue(int i, int j, float value) { 
        adjacencyMatrix[i][j] = value; 
    }
    
    public int[] getFullCurrentTour() { 
        return this.currentTour; 
    }
    
    public int getCurrentTour(int i) { 
        return this.currentTour[i]; 
    }
    
    public void setCurrentTour(int i, int value) { 
        this.currentTour[i] = value; 
    }
    
    public float getCurrentTourEval() { 
        return evaluate(this.currentTour); 
    }
    
    public int getOptimalTour(int i) { 
        return this.optimalTour[i]; 
    }
    
    public void setOptimalTour(int i, int value) { 
        this.optimalTour[i] = value; 
    }
    
    public float getOptimalTourEval() { 
        return evaluate(this.optimalTour); 
    }
}
