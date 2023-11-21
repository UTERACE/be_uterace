package com.be_uterace.utils;

import java.util.ArrayList;
import java.util.List;

public class PolylineDecoder {

    public static String decode(String expression) {
        return decode(expression, 5, false);
    }

    public static String decode(String expression, int precision, boolean geojson) {
        List<double[]> coordinates = new ArrayList<>();
        int index = 0;
        double lat = 0, lng = 0;
        int length = expression.length();
        double factor = Math.pow(10, precision);

        while (index < length) {
            double[] latLngChangeAndIndex = _trans(expression, index);
            double latChange = latLngChangeAndIndex[0];
            index = (int) latLngChangeAndIndex[1];
            double lngChange = _trans(expression, index)[0];
            index = (int) _trans(expression, index)[1];
            lat += latChange;
            lng += lngChange;
            coordinates.add(new double[]{lat / factor, lng / factor});
        }

        StringBuilder result = new StringBuilder("[\n");
        for (double[] coordinate : coordinates) {
            result.append("  [").append(coordinate[0]).append(", ").append(coordinate[1]).append("],\n");
        }
        result.deleteCharAt(result.length() - 2); // Remove the trailing comma
        result.append("]");

        return result.toString();
    }

    private static double[] _trans(String expression, int index) {
        int shift = 0, result = 0;
        int byteRead;
        int byteChunk;

        do {
            byteRead = expression.charAt(index++) - 63;
            byteChunk = byteRead & 0x1F;
            result |= (byteChunk << shift);
            shift += 5;
        } while (byteRead >= 0x20);

        double change = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
        return new double[]{change, index};
    }

    public static void main(String[] args) {
        // Example usage:
        String expression = "gwy`A}cgjSqbBr|J";

        String jsonCoordinates = decode(expression);

        // Print the JSON-like coordinates
        System.out.println(jsonCoordinates);
    }
}
