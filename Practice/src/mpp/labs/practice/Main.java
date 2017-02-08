package mpp.labs.practice;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Main {

    static final Consumer<Object> printer = System.err::println;

    public static void main(String[] args) {

        printer.accept("2. Print countries using forEach");
        Data.COUNTRIES.get()
                .forEach(country -> {
                    System.err.println(country.getName());
                    country.getCities()
                            .stream()
                            .forEach(city -> System.out.printf(" %s%.3fm\n",
                            city.getName(), city.getPopulation())
                            );
                }
                );

        printer.accept("3. Grouping: HashMap<countryName, citiesCount>");
        Map<String, Integer> countryByCitiesCount
                = Data.COUNTRIES.get()
                        .collect(Collectors.toMap(Country::getName, country
                                -> country.getCities().size()));
        countryByCitiesCount
                .forEach((country, cities)
                        -> System.out.printf(" %s%s\n", country, cities));

        printer.accept("4. Grouping: HashMap<countryName, totalPopulation>");
        Map<String, Double> countryByPopulation
                = Data.COUNTRIES.get()
                        .collect(Collectors.toMap(Country::getName, (country)
                                -> country
                                .getCities()
                                .stream()
                                .map(City::getPopulation)
                                .reduce(0.0, Double::sum)
                        ));
        countryByPopulation
                .forEach((country, population)
                        -> System.out.printf(" %s%.2fm\n", country, population));

        printer.accept("5. Find the highlest populated city in each country");
        Map<String, Optional<City>> countryByMostPopulousCity
                = Data.COUNTRIES.get()
                        .collect(Collectors.toMap(Country::getName, Main::findMostPopulousCity));

        countryByMostPopulousCity
                .forEach((country, cityOpt) -> {
                    cityOpt.ifPresent(
                            (city) -> System.out.printf(" %s%s%.1fm\n",
                                    country, city.getName(), city.getPopulation())
                    );
                });

        printer.accept("6. Find the least populated city in each country");
        Data.COUNTRIES.get()
                .collect(Collectors.toMap(Country::getName, Main::findLeastPopulousCity))
                .forEach((country, cityOpt) -> {
                    cityOpt.ifPresent(
                            (city) -> System.out.printf(" %s%s%.1fm\n",
                                    country, city.getName(), city.getPopulation())
                    );
                });

        printer.accept("7. Find whether city exist in the country ");
        String COUNTRY = "Russia";
        String CITY = "Moscow";

        Data.COUNTRIES.get()
                .filter(country -> country.getName().startsWith(COUNTRY))
                .findAny()
                .ifPresent(country
                        -> findCityByName(CITY, country)
                        .ifPresent(city
                                -> System.out.printf(" Found: %s in %s\n",
                                city.getName(), country.getName())
                        )
                );

        printer.accept("8. Find the country, where the name starts with specific character");
        Character CHAR = 'C';
        Data.COUNTRIES.get()
                .filter(country -> country.getName().startsWith(String.valueOf(CHAR)))
                .forEach(country -> System.out.printf(" Found: %s\n", country.getName()));

        printer.accept("9. Find the city population by city name");
        String CITY2 = "Suez";
        Data.COUNTRIES.get()
                .forEach(country
                        -> findCityByName(CITY2, country)
                        .ifPresent(city -> System.out.printf(" Found: %s %.2fm\n",
                        city.getName(), city.getPopulation())));

        printer.accept("10.Print the city and population in sorted order");
        Data.COUNTRIES.get()
                .forEach(country -> {
                    System.err.println(country.getName());
                    country.getCities()
                            .stream()
                            .sorted(Comparator.comparing(City::getPopulation))
                            .forEach(city -> System.out.printf("  %s%.3fm\n",
                            city.getName(), city.getPopulation())
                            );
                });

        printer.accept("11. Print the city and population in reversed order");
        Data.COUNTRIES.get()
                .forEach(country -> {
                    System.err.println(country.getName());
                    country.getCities()
                            .stream()
                            .sorted(Comparator.comparing(City::getPopulation).reversed())
                            .forEach(city -> {
                                System.out.printf(" %s%.3fm\n",
                                        city.getName(), city.getPopulation());
                            });
                });

        printer.accept("12.Find the Summary Statistic of each country");
        Data.COUNTRIES.get()
                .collect(Collectors.toMap(Country::getName, country
                        -> country.getCities().stream()
                        .collect(Collectors.summarizingDouble(City::getPopulation))
                ))
                .forEach((country, stats)
                        -> System.out.printf(" %s%s\n", country, stats));

        printer.accept("13. Find the cities, where the names start with specific character");
        char CHAR2 = 'A';
        Data.COUNTRIES.get()
                .flatMap(country
                        -> country.getCities().stream()
                        .filter(city -> city.getName().startsWith(String.valueOf(CHAR2))))
                .forEach(city
                        -> System.out.printf(" %s%.4fm\n", city.getName(), city.getPopulation()));

        printer.accept("14. Find the cities, which has population > 10 million");
        Data.COUNTRIES.get()
                .flatMap(country
                        -> country.getCities().stream()
                        .filter(city -> city.getPopulation() > 10.0)
                        .sorted(Comparator.comparing(City::getPopulation).reversed())
                )
                .forEach(city
                        -> System.out.printf(" %s%.4fm\n", city.getName(), city.getPopulation()));

        printer.accept("15. Group the cities with their country names by population > 10 million "
                + "mapped into HashMap<CountryName, List<City>>");
        Data.COUNTRIES.get()
                .collect(Collectors.toMap(Country::getName, country
                        -> country.getCities().stream()
                        .filter(city -> city.getPopulation() > 10.0)
                        .sorted(Comparator.comparing(City::getPopulation).reversed())
                        .collect(Collectors.toList()))
                )
                .forEach((country, cities) -> {
                    System.err.println(country);
                    cities.forEach(city -> {
                        System.out.printf("  %s%s\n", city.getName(), city.getPopulation());
                    });
                });

    }

    private static Optional<City> findCityByName(String cityName, Country country) {
        return country
                .getCities()
                .stream()
                .filter((city) -> city.getName().startsWith(cityName))
                .findFirst();
    }

    private static Optional<City> findMostPopulousCity(Country country) {
        return country
                .getCities()
                .stream()
                .max(Comparator.comparing(City::getPopulation));
    }

    private static Optional<City> findLeastPopulousCity(Country country) {
        return country
                .getCities()
                .stream()
                .min(Comparator.comparing(City::getPopulation));
    }
}
