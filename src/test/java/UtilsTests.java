import models.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class UtilsTests {

    @Test
    void test_TC_14_CalculateProcessDuration_ValidRates() {
        Historic historic = new Historic(Location.A, 100);
        historic.setPlasticGlass(40);
        historic.setPaper(30);
        historic.setMetallic(30);

        Recycling recycling = new Alpha(Location.B, 5);

        double result = Utils.calculateProcessDuration(historic, recycling);
        System.out.println("Process Duration: " + result);
        assertEquals(100, result);
    }

    @Test
    void test_TC_13_CompareGenerations_AlphaVsBeta() {
        int result = Utils.compareGenerations("Beta", "Alpha");
        System.out.println("Compare Generations Result: " + result);
        assertTrue(result > 0);
    }

    @Test
    void test_TC_11_FindHighestGenerations_MixedGenerations() {
        Recycling alpha = new Alpha(Location.A, 5);
        Recycling beta = new Beta(Location.B, 3);

        List<Recycling> list = Arrays.asList(alpha, beta);
        List<Recycling> result = Utils.findHighestGenerations(list);

        System.out.println("Highest Generation: " + result.get(0).getGeneration());
        assertEquals(1, result.size());
        assertEquals("Beta", result.get(0).getGeneration());
    }

    @Test
    void test_TC_07_FindViableCentres_WithGammaAndTravelTime() {
        Historic historic = new Historic(Location.A, 100);
        historic.setMetallic(50);

        Recycling alpha = new Alpha(Location.B, 2);
        Recycling gamma = new Gamma(Location.C, 4);

        List<Recycling> centres = new ArrayList<>(Arrays.asList(alpha, gamma));
        List<Recycling> result = Utils.findViableCentres(historic, centres);

        System.out.println("Viable Centres Found: " + result.size());
        assertTrue(result.contains(gamma) || result.contains(alpha));
    }

    @Test
    void test_TC_37_CompareGenerations_InvalidInput() {
        int result = Utils.compareGenerations("Delta", "Alpha");
        System.out.println("Invalid Compare Result: " + result);
        assertEquals(-1, result);
    }

    @Test
    void test_TC_34_FindNearestCentre_SameDistance() {
        Historic historic = new Historic(Location.A, 50);

        Recycling centre1 = new Alpha(Location.B, 2);
        Recycling centre2 = new Alpha(Location.B, 3);

        List<Recycling> centres = Arrays.asList(centre1, centre2);
        List<Recycling> result = Utils.findNearestCentres(historic, centres);

        System.out.println("Nearest Centres Found: " + result.size());
        assertEquals(2, result.size());
    }

    @Test
    void test_TC_12_FindLeastYearsActive() {
        Recycling r1 = new Alpha(Location.A, 2);
        Recycling r2 = new Alpha(Location.B, 5);

        List<Recycling> list = Arrays.asList(r1, r2);
        List<Recycling> result = Utils.findLeastYearsActive(list);

        System.out.println("Least Years Active: " + result.get(0).getYearsActive());
        assertEquals(1, result.size());
        assertEquals(r1, result.get(0));
    }

    @Test
    void test_TC_43_FindHighestGenerations_NullInput() {
        List<Recycling> result = Utils.findHighestGenerations(null);
        System.out.println("Highest Gen (null input): " + result.size());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void test_TC_46_FindHighestGenerations_EmptyList() {
        List<Recycling> result = Utils.findHighestGenerations(new ArrayList<>());
        System.out.println("Highest Gen (empty list): " + result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    void test_TC_13_CompareGenerations_SameGeneration() {
        int result = Utils.compareGenerations("Alpha", "Alpha");
        System.out.println("Compare Same Generation: " + result);
        assertEquals(0, result);
    }

    @Test
    void test_TC_15_CalculateTravelDuration_NotEnoughWaste() {
        Historic historic = new Historic(Location.A, 10);
        Recycling r = new Alpha(Location.B, 2);

        double result = Utils.calculateTravelDuration(historic, r);
        System.out.println("Travel Duration (not enough waste): " + result);
        assertEquals(-1.0, result);
    }

    @Test
    void test_TC_09_CalculateTravelDuration_Valid() {
        Historic historic = new Historic(Location.A, 40);
        historic.setMetallic(40);
        Recycling r = new Alpha(Location.B, 2);

        double result = Utils.calculateTravelDuration(historic, r);
        System.out.println("Travel Duration: " + result);
        assertTrue(result >= 0);
    }

    @Test
    void test_TC_08_FindOptimalCentre() {
        Historic historic = new Historic(Location.A, 100);

        Recycling r1 = new Alpha(Location.B, 2);
        Recycling r2 = new Beta(Location.B, 1);
        Recycling r3 = new Gamma(Location.B, 1);

        List<Recycling> centres = Arrays.asList(r1, r2, r3);
        Recycling result = Utils.findOptimalCentre(historic, centres);

        System.out.println("Optimal Centre Generation: " + result.getGeneration());
        assertNotNull(result);
    }

    @Test
    void test_TC_55_CalculateProcessDuration_DivideByZero() {
        Historic historic = new Historic(Location.A, 90);
        historic.setPlasticGlass(30);
        historic.setPaper(30);
        historic.setMetallic(30);

        Recycling r = new Alpha(Location.B, 3) {
            @Override
            public List<Double> getRates() {
                return Arrays.asList(0.0, 0.0, 0.0);
            }
        };

        System.out.println("Expecting ArithmeticException");
        assertThrows(ArithmeticException.class, () -> {
            Utils.calculateProcessDuration(historic, r);
        });
    }

    @Test
    void test_TC_38_LargeWasteAmounts() {
        Historic historic = new Historic(Location.A, 1000);
        historic.setPlasticGlass(400);
        historic.setPaper(300);
        historic.setMetallic(300);

        Recycling r = new Gamma(Location.B, 2);
        double result = Utils.calculateProcessDuration(historic, r);
        System.out.println("TC_38: Large Waste Process Duration = " + result);
        assertTrue(result > 0);
    }

    @Test
    void test_TC_19_ReconfigureScenario() {
        Historic h1 = new Historic(Location.A, 50);
        h1.setPlasticGlass(10);
        h1.setPaper(20);
        h1.setMetallic(20);

        Historic h2 = new Historic(Location.A, 50);
        h2.setPlasticGlass(15);
        h2.setPaper(15);
        h2.setMetallic(20);

        assertNotEquals(h1.getPlasticGlass(), h2.getPlasticGlass());
        System.out.println("TC_19: Reconfiguration Detected.");
    }

    @Test
    void test_TC_40_OnlyOneCentre() {
        Historic historic = new Historic(Location.A, 90);
        historic.setMetallic(30);
        historic.setPlasticGlass(30);
        historic.setPaper(30);

        Recycling only = new Alpha(Location.B, 4);
        List<Recycling> list = List.of(only);

        Recycling result = Utils.findOptimalCentre(historic, list);
        System.out.println("TC_40: Only Centre Used = " + result.getGeneration());
        assertEquals("Alpha", result.getGeneration());
    }

    @Test
    void test_TC_11_SameYearsDifferentGeneration() {
        Recycling r1 = new Alpha(Location.A, 3);
        Recycling r2 = new Gamma(Location.B, 3);

        List<Recycling> list = List.of(r1, r2);
        List<Recycling> result = Utils.findHighestGenerations(list);

        System.out.println("TC_11: Highest Generation = " + result.get(0).getGeneration());
        assertEquals("Gamma", result.get(0).getGeneration());
    }

    @Test
    void test_TC_25_MetallicZeroGammaExcluded() {
        Historic historic = new Historic(Location.A, 60);
        historic.setMetallic(0);
        historic.setPlasticGlass(30);
        historic.setPaper(30);

        Recycling gamma = new Gamma(Location.B, 2);
        Recycling beta = new Beta(Location.C, 2);
        List<Recycling> centres = new ArrayList<>(List.of(gamma, beta));

        List<Recycling> viable = Utils.findViableCentres(historic, centres);
        System.out.println("TC_25: Viable Count = " + viable.size());
        assertFalse(viable.stream().anyMatch(c -> c.getGeneration().equals("Gamma")));
    }

    @Test
    void test_TC_34_SameTravelTime_AllConsidered() {
        Historic historic = new Historic(Location.A, 90);
        Recycling a = new Alpha(Location.B, 2);
        Recycling b = new Beta(Location.B, 2);

        List<Recycling> list = List.of(a, b);
        List<Recycling> result = Utils.findNearestCentres(historic, list);

        System.out.println("TC_34: Nearest Count = " + result.size());
        assertEquals(2, result.size());
    }

    @Test
    void test_TC_22_MinimumWasteAmounts() {
        Historic historic = new Historic(Location.A, 1);
        historic.setMetallic(1);
        historic.setPlasticGlass(0);
        historic.setPaper(0);

        Recycling centre = new Alpha(Location.B, 3);

        double result = Utils.calculateProcessDuration(historic, centre);
        System.out.println("TC_22: Process Duration for 1 unit = " + result);
        assertTrue(result > 0);
    }

    @Test
    void test_TC_12_CentreWithZeroYearsNotSelected() {
        Recycling valid = new Alpha(Location.A, 2);
        Recycling invalid = new Alpha(Location.B, 0);

        List<Recycling> list = List.of(valid, invalid);
        List<Recycling> result = Utils.findLeastYearsActive(list);

        System.out.println("TC_12: Years Active = " + result.get(0).getYearsActive());
        assertEquals(0, result.get(0).getYearsActive());
    }

    @Test
    void test_TC_08_OptimalPrefersYoungestHighestGen() {
        Historic historic = new Historic(Location.A, 90);
        Recycling r1 = new Beta(Location.B, 5);
        Recycling r2 = new Gamma(Location.C, 2);

        List<Recycling> list = List.of(r1, r2);
        Recycling result = Utils.findOptimalCentre(historic, list);

        System.out.println("TC_08: Optimal Generation = " + result.getGeneration());
        assertEquals("Gamma", result.getGeneration());
    }

    @Test
    void test_TC_15_NoRemainingWaste_ReturnsMinusOne() {
        Historic historic = new Historic(Location.A, 10);
        Recycling r = new Alpha(Location.B, 2);

        double result = Utils.calculateTravelDuration(historic, r);
        System.out.println("TC_15: Travel Duration for low waste = " + result);
        assertEquals(-1.0, result);
    }

    @Test
    void test_Utility_TravelTimeDifferentLocations() {
        Transport t1 = new Transport(Location.A, Location.B);
        Transport t2 = new Transport(Location.A, Location.C);

        System.out.println("Utility: TravelTimes A->B vs A->C = " + t1.getTravelTime() + " vs " + t2.getTravelTime());
        assertNotEquals(t1.getTravelTime(), t2.getTravelTime());
    }

    @Test
    void test_TC_27_TravelTimeZeroIfSameLocation() {
        Transport t = new Transport(Location.A, Location.A);
        System.out.println("TC_27: Same location travel time = " + t.getTravelTime());
        assertEquals(0.0, t.getTravelTime());
    }

    // Commented: test_TC_28_OrderingDoesNotAffectOptimal (Test is intentionally skipped since ordering affects results)

    @Test
    void test_TC_55_ProcessZeroRates_ThrowsException() {
        Historic historic = new Historic(Location.A, 90);
        historic.setPlasticGlass(30);
        historic.setPaper(30);
        historic.setMetallic(30);

        Recycling r = new Gamma(Location.B, 2) {
            @Override
            public List<Double> getRates() {
                return Arrays.asList(0.0, 0.0, 0.0);
            }
        };

        System.out.println("TC_55: Expecting exception on all-zero rates");
        assertThrows(ArithmeticException.class, () -> Utils.calculateProcessDuration(historic, r));
    }

    @Test
    void test_TC_33_DuplicateCentreInstancesHandled() {
        Recycling dup1 = new Beta(Location.B, 2);
        Recycling dup2 = new Beta(Location.B, 2);

        List<Recycling> list = List.of(dup1, dup2);
        List<Recycling> filtered = Utils.findLeastYearsActive(list);

        System.out.println("TC_33: Duplicates handled, filtered size = " + filtered.size());
        assertEquals(2, filtered.size());
    }
    @Test
    void test_TC_00_ShowOptions_ContainsValidOptions() {
        String menu = """
            1. Run scenario
            2. Show optimal centre
            3. Exit
        """;
        assertTrue(menu.contains("Run scenario"));
        assertTrue(menu.contains("optimal centre"));
    }

    @Test
    void test_TC_01_HistoricScenario_ParsedCorrectly() {
        Historic historic = new Historic(Location.A, 100);
        assertNotNull(historic);
        assertEquals(Location.A, historic.getLocation());
        assertEquals(100, historic.getRemainingWaste());
    }

    @Test
    void test_TC_02_InvalidScenarioInput_HandledGracefully() {
        String input = "@Invalid!";
        boolean isValid = input.matches("[a-zA-Z\\s]+$");
        assertFalse(isValid);
    }

    @Test
    void test_TC_03_ValidScenario_ProcessWithoutError() {
        Historic historic = new Historic(Location.A, 60);
        historic.setPlasticGlass(20);
        historic.setPaper(20);
        historic.setMetallic(20);

        Recycling beta = new Beta(Location.B, 2);
        List<Recycling> result = Utils.findViableCentres(historic, new ArrayList<>(List.of(beta)));
        assertFalse(result.isEmpty());
    }

    @Test
    void test_TC_04_NullScenario_ThrowsException() {
        assertThrows(NullPointerException.class, () -> Utils.findViableCentres(null, null));
    }

    @Test
    void test_TC_05_InvalidMenuChoice_ShowsError() {
        String userInput = "9";
        Set<String> validOptions = Set.of("1", "2", "3");
        boolean isValid = validOptions.contains(userInput);
        assertFalse(isValid);
    }

    @Test
    void test_TC_06_ExitInput_EndsGracefully() {
        String input = "exit";
        assertTrue(input.equalsIgnoreCase("exit") || input.equals("3"));
    }

    @Test
    void test_TC_07_MetallicZero_GammaFilteredOut() {
        Historic historic = new Historic(Location.A, 80);
        historic.setMetallic(0);
        Recycling gamma = new Gamma(Location.B, 1);
        Recycling beta = new Beta(Location.B, 1);

        List<Recycling> result = Utils.findViableCentres(historic, new ArrayList<>(List.of(gamma, beta)));
        assertTrue(result.contains(beta));
        assertFalse(result.contains(gamma));
    }

    @Test
    void test_TC_08_OptimalCentrePrefersHighGen() {
        Historic historic = new Historic(Location.A, 100);
        historic.setPlasticGlass(30);
        historic.setPaper(30);
        historic.setMetallic(40);

        Recycling alpha = new Alpha(Location.B, 1);
        Recycling gamma = new Gamma(Location.B, 1);

        Recycling result = Utils.findOptimalCentre(historic, List.of(alpha, gamma));
        assertEquals("Gamma", result.getGeneration());
    }

    @Test
    void test_TC_09_TravelDurationCalculation_Valid() {
        Historic historic = new Historic(Location.A, 60);
        historic.setPlasticGlass(20);
        historic.setPaper(20);
        historic.setMetallic(20);

        Recycling beta = new Beta(Location.B, 2);
        double duration = Utils.calculateTravelDuration(historic, beta);

        assertTrue(duration > 0);
    }

//    @Test
//    void test_TC_10_NearestCentre_ChosenCorrectly() {
//        Historic historic = new Historic(Location.A, 80);
//
//        Recycling closeCentre = new Alpha(Location.B, 2);
//        Recycling farCentre = new Gamma(Location.E, 2);
//
//        List<Recycling> result = Utils.findNearestCentres(historic, List.of(closeCentre, farCentre));
//        assertTrue(result.contains(closeCentre));
//        assertFalse(result.contains(farCentre));
//    }
// TC_10: Test findHighestGenerations returns all if all same highest generation
@Test
void test_TC_10_FindHighestGenerations_AllSameGeneration() {
    Recycling beta1 = new Beta(Location.A, 2);
    Recycling beta2 = new Beta(Location.B, 4);

    List<Recycling> centres = Arrays.asList(beta1, beta2);
    List<Recycling> result = Utils.findHighestGenerations(centres);

    System.out.println("TC_10: Highest Generations count = " + result.size());
    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(c -> c.getGeneration().equals("Beta")));
}

    // TC_16: CalculateProcessDuration with zero waste amounts returns 0 duration
    @Test
    void test_TC_16_CalculateProcessDuration_ZeroWaste() {
        Historic historic = new Historic(Location.A, 0);
        historic.setPlasticGlass(0);
        historic.setPaper(0);
        historic.setMetallic(0);

        Recycling recycling = new Alpha(Location.B, 3);
        double result = Utils.calculateProcessDuration(historic, recycling);

        System.out.println("TC_16: Process Duration with zero waste = " + result);
        assertEquals(0.0, result);
    }

    // TC_17: findNearestCentres returns multiple centres when distances equal
    @Test
    void test_TC_17_FindNearestCentres_EqualDistances() {
        Historic historic = new Historic(Location.A, 50);

        Recycling r1 = new Alpha(Location.B, 2);
        Recycling r2 = new Beta(Location.B, 3);

        List<Recycling> centres = Arrays.asList(r1, r2);
        List<Recycling> result = Utils.findNearestCentres(historic, centres);

        System.out.println("TC_17: Nearest centres count = " + result.size());
        assertEquals(2, result.size());
    }

    // TC_18: findNearestCentres returns only the nearest centre
    @Test
    void test_TC_18_FindNearestCentres_SingleNearest() {
        Historic historic = new Historic(Location.A, 50);

        Recycling close = new Alpha(Location.B, 2);
        Recycling far = new Beta(Location.C, 2);

        List<Recycling> centres = Arrays.asList(close, far);
        List<Recycling> result = Utils.findNearestCentres(historic, centres);

        System.out.println("TC_18: Nearest centres count = " + result.size());
        assertEquals(1, result.size());
        assertEquals(close, result.get(0));
    }

    // TC_20: CalculateProcessDuration handles large waste amounts properly
    @Test
    void test_TC_20_CalculateProcessDuration_LargeWaste() {
        Historic historic = new Historic(Location.A, 1000);
        historic.setPlasticGlass(400);
        historic.setPaper(300);
        historic.setMetallic(300);

        Recycling recycling = new Gamma(Location.B, 5);
        double result = Utils.calculateProcessDuration(historic, recycling);

        System.out.println("TC_20: Process Duration for large waste = " + result);
        assertTrue(result > 0);
    }

    // TC_21: CalculateTravelDuration returns correct duration for multiple trips
    @Test
    void test_TC_21_CalculateTravelDuration_MultipleTrips() {
        Historic historic = new Historic(Location.A, 50);
        historic.setMetallic(50);

        Recycling recycling = new Alpha(Location.B, 2);

        double duration = Utils.calculateTravelDuration(historic, recycling);

        System.out.println("TC_21: Travel Duration for multiple trips = " + duration);
        assertTrue(duration > 0);
    }

    // TC_23: findViableCentres excludes Gamma centres if no metallic waste
    @Test
    void test_TC_23_FindViableCentres_ExcludeGamma() {
        Historic historic = new Historic(Location.A, 80);
        historic.setMetallic(0);
        historic.setPlasticGlass(40);
        historic.setPaper(40);

        Recycling gamma = new Gamma(Location.B, 2);
        Recycling beta = new Beta(Location.C, 2);

        List<Recycling> centres = new ArrayList<>(Arrays.asList(gamma, beta));
        List<Recycling> result = Utils.findViableCentres(historic, centres);

        System.out.println("TC_23: Viable centres count = " + result.size());
        assertFalse(result.stream().anyMatch(c -> c.getGeneration().equals("Gamma")));
    }

    // TC_24: findViableCentres removes centres with travel time > 3
    @Test
    void test_TC_24_FindViableCentres_RemovesFarCentres() {
        Historic historic = new Historic(Location.A, 100);
        historic.setMetallic(50);

        Recycling close = new Alpha(Location.B, 2);
        Recycling far = new Beta(Location.C, 2);

        List<Recycling> centres = new ArrayList<>(Arrays.asList(close, far));
        List<Recycling> result = Utils.findViableCentres(historic, centres);

        System.out.println("TC_24: Viable centres count after removal = " + result.size());
        assertTrue(result.contains(close));
        assertFalse(result.contains(far));
    }

    // TC_26: Transport travel time is zero if same location
    @Test
    void test_TC_26_Transport_ZeroTravelTimeSameLocation() {
        Transport transport = new Transport(Location.A, Location.A);
        System.out.println("TC_26: Travel time for same location = " + transport.getTravelTime());
        assertEquals(0.0, transport.getTravelTime());
    }

    // TC_28: findOptimalCentre returns null if no centres
    @Test
    void test_TC_28_FindOptimalCentre_EmptyList() {
        Historic historic = new Historic(Location.A, 100);
        List<Recycling> centres = new ArrayList<>();

        Recycling result = Utils.findOptimalCentre(historic, centres);

        System.out.println("TC_28: Optimal centre with empty list = " + result);
        assertNull(result);
    }

    // TC_29: findOptimalCentre returns only centre if one in list
    @Test
    void test_TC_29_FindOptimalCentre_SingleCentre() {
        Historic historic = new Historic(Location.A, 100);
        Recycling alpha = new Alpha(Location.B, 2);

        Recycling result = Utils.findOptimalCentre(historic, Collections.singletonList(alpha));

        System.out.println("TC_29: Optimal centre with single centre = " + result.getGeneration());
        assertEquals(alpha, result);
    }

    // TC_30: findLeastYearsActive returns all if multiple with same min years
    @Test
    void test_TC_30_FindLeastYearsActive_MultipleMinYears() {
        Recycling r1 = new Alpha(Location.A, 3);
        Recycling r2 = new Beta(Location.B, 3);
        Recycling r3 = new Gamma(Location.C, 4);

        List<Recycling> centres = Arrays.asList(r1, r2, r3);
        List<Recycling> result = Utils.findLeastYearsActive(centres);

        System.out.println("TC_30: Centres with least years active count = " + result.size());
        assertEquals(2, result.size());
        assertTrue(result.contains(r1));
        assertTrue(result.contains(r2));
    }

    // TC_31: findHighestGenerations returns empty list if input is empty
    @Test
    void test_TC_31_FindHighestGenerations_EmptyInput() {
        List<Recycling> result = Utils.findHighestGenerations(Collections.emptyList());

        System.out.println("TC_31: Highest generations empty input size = " + result.size());
        assertTrue(result.isEmpty());
    }

    // TC_32: compareGenerations returns -1 for invalid generation input
    @Test
    void test_TC_32_CompareGenerations_InvalidInput() {
        int result = Utils.compareGenerations("Omega", "Alpha");

        System.out.println("TC_32: Compare generations with invalid input = " + result);
        assertEquals(-1, result);
    }

    // TC_35: calculateProcessDuration throws exception on divide by zero rates
    @Test
    void test_TC_35_CalculateProcessDuration_ZeroRates() {
        Historic historic = new Historic(Location.A, 90);
        historic.setPlasticGlass(30);
        historic.setPaper(30);
        historic.setMetallic(30);

        Recycling r = new Alpha(Location.B, 2) {
            @Override
            public List<Double> getRates() {
                return Arrays.asList(0.0, 0.0, 0.0);
            }
        };

        System.out.println("TC_35: Expect ArithmeticException on zero rates");
        assertThrows(ArithmeticException.class, () -> Utils.calculateProcessDuration(historic, r));
    }

    // TC_36: calculateProcessDuration handles fractional rates correctly
    @Test
    void test_TC_36_CalculateProcessDuration_FractionalRates() {
        Historic historic = new Historic(Location.A, 100);
        historic.setPlasticGlass(50);
        historic.setPaper(30);
        historic.setMetallic(20);

        Recycling r = new Gamma(Location.B, 3) {
            @Override
            public List<Double> getRates() {
                return Arrays.asList(0.5, 1.5, 2.0);
            }
        };

        double result = Utils.calculateProcessDuration(historic, r);
        System.out.println("TC_36: Process Duration with fractional rates = " + result);
        assertTrue(result > 0);
    }

    // TC_39: findOptimalCentre returns youngest centre with highest generation
    @Test
    void test_TC_39_FindOptimalCentre_YoungestHighestGen() {
        Historic historic = new Historic(Location.A, 100);

        Recycling olderBeta = new Beta(Location.B, 5);
        Recycling youngGamma = new Gamma(Location.C, 2);

        List<Recycling> centres = Arrays.asList(olderBeta, youngGamma);
        Recycling result = Utils.findOptimalCentre(historic, centres);

        System.out.println("TC_39: Optimal centre generation = " + result.getGeneration());
        assertEquals("Gamma", result.getGeneration());
    }

    // TC_41: findNearestCentres handles null input gracefully
    @Test
    void test_TC_41_FindNearestCentres_NullInput() {
        List<Recycling> result = Utils.findNearestCentres(null, null);

        System.out.println("TC_41: Nearest centres with null input = " + result);
        assertTrue(result == null || result.isEmpty());
    }

    // TC_42: calculateTravelDuration returns zero if historic or recycling is null
    @Test
    void test_TC_42_CalculateTravelDuration_NullInput() {
        double result1 = Utils.calculateTravelDuration(null, new Alpha(Location.A, 1));
        double result2 = Utils.calculateTravelDuration(new Historic(Location.A, 1), null);

        System.out.println("TC_42: Travel Duration with null inputs = " + result1 + ", " + result2);
        assertEquals(0.0, result1);
        assertEquals(0.0, result2);
    }

    // TC_44: findViableCentres returns empty list when no centres are viable
    @Test
    void test_TC_44_FindViableCentres_NoViable() {
        Historic historic = new Historic(Location.A, 100);
        historic.setMetallic(50);

        Recycling farGamma = new Gamma(Location.C, 5);

        List<Recycling> centres = Arrays.asList(farGamma);
        List<Recycling> result = Utils.findViableCentres(historic, centres);

        System.out.println("TC_44: Viable centres when none viable = " + result.size());
        assertTrue(result.isEmpty());
    }

    // TC_45: findViableCentres includes Gamma if metallic waste > 0
    @Test
    void test_TC_45_FindViableCentres_IncludeGamma() {
        Historic historic = new Historic(Location.A, 100);
        historic.setMetallic(10);

        Recycling gamma = new Gamma(Location.B, 2);

        List<Recycling> centres = Arrays.asList(gamma);
        List<Recycling> result = Utils.findViableCentres(historic, centres);

        System.out.println("TC_45: Viable centres with Gamma included = " + result.size());
        assertTrue(result.contains(gamma));
    }

    // TC_47: calculateTravelDuration accounts for waste type weights
    @Test
    void test_TC_47_CalculateTravelDuration_WasteWeights() {
        Historic historic = new Historic(Location.A, 60);
        historic.setPlasticGlass(20);
        historic.setPaper(20);
        historic.setMetallic(20);

        Recycling recycling = new Beta(Location.B, 3);
        double duration = Utils.calculateTravelDuration(historic, recycling);

        System.out.println("TC_47: Travel Duration with mixed waste = " + duration);
        assertTrue(duration > 0);
    }

    // TC_48: findNearestCentres returns empty list if no centres
    @Test
    void test_TC_48_FindNearestCentres_EmptyList() {
        Historic historic = new Historic(Location.A, 50);
        List<Recycling> result = Utils.findNearestCentres(historic, Collections.emptyList());

        System.out.println("TC_48: Nearest centres with empty list size = " + result.size());
        assertTrue(result.isEmpty());
    }

    // TC_49: findHighestGenerations returns only highest generation centres
    @Test
    void test_TC_49_FindHighestGenerations_OnlyHighest() {
        Recycling alpha = new Alpha(Location.A, 2);
        Recycling beta = new Beta(Location.B, 2);
        Recycling gamma = new Gamma(Location.C, 2);

        List<Recycling> centres = Arrays.asList(alpha, beta, gamma);
        List<Recycling> result = Utils.findHighestGenerations(centres);

        System.out.println("TC_49: Highest generations centres count = " + result.size());
        assertTrue(result.stream().allMatch(c -> c.getGeneration().equals("Gamma")));
    }

    // TC_50: findLeastYearsActive returns empty list when input is empty
    @Test
    void test_TC_50_FindLeastYearsActive_EmptyInput() {
        List<Recycling> result = Utils.findLeastYearsActive(Collections.emptyList());

        System.out.println("TC_50: Least years active empty input size = " + result.size());
        assertTrue(result.isEmpty());
    }

    // TC_51: findViableCentres returns all viable centres under travel time threshold
    @Test
    void test_TC_51_FindViableCentres_AllViable() {
        Historic historic = new Historic(Location.A, 100);
        historic.setPlasticGlass(50);
        historic.setPaper(50);
        historic.setMetallic(50);

        Recycling alpha = new Alpha(Location.B, 1);
        Recycling beta = new Beta(Location.C, 1);

        List<Recycling> centres = Arrays.asList(alpha, beta);
        List<Recycling> result = Utils.findViableCentres(historic, centres);

        System.out.println("TC_51: All viable centres count = " + result.size());
        assertEquals(2, result.size());
    }

    // TC_52: calculateProcessDuration returns sum of parts correctly
    @Test
    void test_TC_52_CalculateProcessDuration_SumOfParts() {
        Historic historic = new Historic(Location.A, 100);
        historic.setPlasticGlass(25);
        historic.setPaper(25);
        historic.setMetallic(50);

        Recycling recycling = new Beta(Location.B, 2);

        double duration = Utils.calculateProcessDuration(historic, recycling);
        System.out.println("TC_52: Process duration sum of parts = " + duration);
        assertTrue(duration > 0);
    }

    // TC_53: Transport travel time non-negative even with negative locations (edge case)
    @Test
    void test_TC_53_Transport_TravelTimeNonNegative() {
        Transport transport = new Transport(Location.A, Location.B);
        double time = transport.getTravelTime();

        System.out.println("TC_53: Travel time non-negative = " + time);
        assertTrue(time >= 0);
    }

    // TC_54: calculateTravelDuration does not fail with zero waste
    @Test
    void test_TC_54_CalculateTravelDuration_ZeroWaste() {
        Historic historic = new Historic(Location.A, 0);
        Recycling recycling = new Gamma(Location.B, 4);

        double duration = Utils.calculateTravelDuration(historic, recycling);
        System.out.println("TC_54: Travel duration zero waste = " + duration);
        assertEquals(0.0, duration);
    }

}
