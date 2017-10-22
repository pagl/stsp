/*
 * Mateusz Ledzianowski inf117226
 * Patryk Gliszczynski inf117288
 */
package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class DataParser {

    private static final String NAME = "NAME";
    private static final String TYPE = "TYPE";
    private static final String COMMENT = "COMMENT";
    private static final String DIMENSION = "DIMENSION";
    private static final String EDGE_WEIGHT_TYPE = "EDGE_WEIGHT_TYPE";
    private static final String EDGE_WEIGHT_FORMAT = "EDGE_WEIGHT_FORMAT";
    private static final String EDGE_WEIGHT_SECTION = "EDGE_WEIGHT_SECTION";
    private static final String DISPLAY_DATA_TYPE = "DISPLAY_DATA_TYPE";
    private static final String DISPLAY_DATA_SECTION = "DISPLAY_DATA_SECTION";
    private static final String NODE_COORD_SECTION = "NODE_COORD_SECTION";
    private static final String TOUR_SECTION = "TOUR_SECTION";
    private static final String EOF = "EOF";
    

    public static void parse(String filePath, Data data) throws IOException, FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            State state = State.DESCRIPTION;
            String line;
            String[] row;
            int i = 0;
            while (lineContinue(line = br.readLine())) {
                if (!line.isEmpty()) {
                    switch (state) {
                        case DESCRIPTION:
                            row = line.split(":");
                            switch (row[0].trim()) {
                                case NAME:
                                    data.setName(row[1].trim());
                                    break;
                                case TYPE:
                                    data.setType(row[1].trim());
                                    break;
                                case COMMENT:
                                    data.setComment(row[1].trim());
                                    break;
                                case DIMENSION:
                                    data.setDimension(Integer.parseInt(row[1].trim()));
                                    break;
                                case EDGE_WEIGHT_TYPE:
                                    data.setEdgeWeightType(row[1].trim());
                                    break;
                                case EDGE_WEIGHT_FORMAT:
                                    data.setEdgeWeightFormat(row[1].trim());
                                    break;
                                case DISPLAY_DATA_TYPE:
                                    data.setDisplayDataTime(row[1].trim());
                                    break;
                                case NODE_COORD_SECTION:
                                    state = State.NODE_COORD_SECTION;
                                    break;
                                case TOUR_SECTION:
                                    state = State.TOUR_SECTION;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case NODE_COORD_SECTION:
                            row = line.split(" ");
                            data.setNodeCoord(
                                    Integer.parseInt(row[0].trim()),
                                    Float.parseFloat(row[1].trim()),
                                    Float.parseFloat(row[2].trim()));
                            break;
                        case TOUR_SECTION:
                            data.setOptimalTour(i++, Integer.parseInt(line.trim()));
                            break;
                        default:
                            break;
                    }
                }
            }
            data.initData();
        }
    }

    private static boolean lineContinue(String readLine) {
        return readLine != null
                && !readLine.trim().equals("-1")
                && !readLine.trim().equals(EOF);
    }

    private enum State {
        DESCRIPTION, NODE_COORD_SECTION, TOUR_SECTION
    }
}
