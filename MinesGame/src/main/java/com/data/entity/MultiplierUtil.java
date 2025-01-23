package com.data.entity;

public class MultiplierUtil {

    // Example multiplier table
	private static final double[][] MULTIPLIERS = {
		    // Row for 1 mine
		    {1.01, 1.08, 1.12, 1.18, 1.24, 1.3, 1.37, 1.46, 1.55, 1.65, 1.77, 1.9, 2.06, 2.25, 2.47, 2.75, 3.09, 3.54, 4.12, 4.95, 6.19, 8.25, 12.37, 24.75},
		    
		    // Row for 2 mines
		    {1.08, 1.17, 1.29, 1.42, 1.56, 1.74, 1.94, 2.18, 2.47, 2.83, 3.26, 3.81, 4.5, 5.46, 6.6, 8.25, 10.61, 14.14, 19.8, 29.7, 49.5, 99.0, 297.0},
		    
		    // Row for 3 mines
		    {1.12, 1.29, 1.48, 1.71, 2.0, 2.35, 2.79, 3.35, 4.07, 5.06, 6.26, 7.96, 10.35, 13.85, 18.97, 27.11, 40.66, 65.06, 113.85, 227.7, 569.3, 2277.0},
		    
		    // Row for 4 mines
		    {1.18, 1.41, 1.71, 2.09, 2.58, 3.09, 3.76, 4.61, 5.76, 7.26, 9.19, 11.83, 15.5, 20.85, 27.99, 39.35, 56.87, 94.63, 142.33, 569.3, 12523.0},
		    
		    // Row for 5 mines
		    {1.24, 1.56, 2.09, 2.58, 3.2, 4.01, 5.26, 6.88, 9.17, 12.25, 16.65, 24.47, 40.87, 58.38, 102.17, 189.75, 273.95, 834.9, 2504.0, 52598.0},
		    
		    // Row for 6 mines
		    {1.3, 1.74, 2.35, 2.79, 3.63, 4.57, 6.46, 9.21, 14.17, 21.89, 33.63, 58.38, 100.47, 166.4, 277.33, 379.5, 2087.0, 6261.0, 92598.0},
		    
		    // Row for 7 mines
		    {1.37, 1.94, 2.79, 3.35, 4.61, 6.46, 9.94, 14.17, 24.47, 39.95, 73.95, 138.66, 356.56, 600.87, 1442.0, 3965.0, 13219.0, 475893.0},
		    
		    // Row for 8 mines
		    {1.46, 2.18, 3.35, 4.07, 6.19, 9.21, 14.47, 24.47, 40.66, 83.2, 176.84, 356.56, 831.98, 2163.0, 6489.0, 18973.0},
		    
		    // Row for 9 mines
		    {1.55, 2.47, 4.07, 5.26, 9.94, 14.47, 24.47, 44.85, 83.2, 166.4, 404.1, 1010.0, 2828.0, 9193.0, 36773.0, 202545.0},
		    
		    // Row for 10 mines
		    {1.65, 2.83, 5.06, 6.88, 14.47, 24.47, 44.85, 83.2, 166.4, 404.1, 1010.0, 3232.0, 12113.0, 49831.0, 237945.0},
		    
		    // Row for 11 mines
		    {1.77, 3.26, 6.26, 9.19, 24.47, 44.85, 83.2, 166.4, 404.1, 1010.0, 3232.0, 12113.0, 56574.0, 367735.0},
		    
		    // Row for 12 mines
		    {1.9, 3.81, 7.96, 11.83, 40.87, 73.95, 138.66, 356.56, 831.98, 2163.0, 6489.0, 367735.0},
		    
		    // Row for 13 mines
		    {2.06, 4.5, 10.35, 15.5, 58.38, 138.66, 277.33, 600.87, 1442.0, 3965.0, 118973.0},
		    
		    // Row for 14 mines
		    {2.25, 5.46, 13.85, 20.85, 102.17, 2163.0, 4736.0, 1442.0, 5968.0, 237945.0},
		    
		    // Row for 15 mines
		    {2.47, 6.6, 15.97, 27.99, 144.7, 3795.0, 118973.0},
		    
		    // Row for 16 mines
		    {2.75, 8.25, 27.11, 39.35, 2087.0, 3965.0},
		    
		    // Row for 17 mines
		    {3.09, 10.61, 40.66, 56.87, 3795.0},
		    
		    // Row for 18 mines
		    {3.54, 14.14, 65.06, 94.63, 118973.0},
		    
		    // Row for 19 mines
		    {4.12, 19.8, 113.85, 142.33},
		    
		    // Row for 20 mines
		    {4.95, 29.7, 227.7, 569.3},
		    
		    // Row for 21 mines
		    {6.19, 49.5, 569.3},
		    
		    // Row for 22 mines
		    {8.25, 99.0, 2277.0},
		    
		    // Row for 23 mines
		    {12.37, 297.0},
		    
		    // Row for 24 mines
		    {24.75}
		};

    public static double getMultiplier(int mines, int diamonds) {
        // Validate input: mines must be at least 1
        if (mines < 1) {
            throw new IllegalArgumentException("Number of mines must be at least 1.");
        }

        // Cap the number of mines to the maximum defined rows
        int maxMines = MULTIPLIERS.length;
        if (mines > maxMines) {
            mines = maxMines; // Cap mines to the highest available row
        }

        // Validate input: diamonds must be at least 1
        if (diamonds < 1) {
            throw new IllegalArgumentException("Number of revealed diamonds must be at least 1.");
        }

        // Get the maximum diamonds allowed for the given mine count
        int maxRevealed = MULTIPLIERS[mines - 1].length;
        if (diamonds > maxRevealed) {
            diamonds = maxRevealed; // Cap diamonds to the maximum available value
        }

        // Return the multiplier value from the table
        return MULTIPLIERS[mines - 1][diamonds - 1];
    }
}
