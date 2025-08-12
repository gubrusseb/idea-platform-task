package ru.russeb;

import com.google.gson.Gson;
import ru.russeb.model.Ticket;
import ru.russeb.model.TicketWrapper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App
{
    public static void main( String[] args ) throws FileNotFoundException {
        Gson gson = new Gson();

        String origin = "VVO";
        String destination = "TLV";

        TicketWrapper ticketWrapper = gson.fromJson(new FileReader("tickets.json"), TicketWrapper.class);
        List<Ticket> filteredTickets = ticketWrapper.getTickets()
                .stream()
                .filter(ticket -> ticket.getOrigin().equals(origin) && ticket.getDestination().equals(destination))
                .toList();


        Map<String,Long> minimalFlightTimeByEachCarrier = getMinimalFlightTimeByEachCarrier(filteredTickets);
        double difference = getDifferenceMeanMedian(filteredTickets);

        outputResult(minimalFlightTimeByEachCarrier, difference);
        }

    private static Map<String,Long> getMinimalFlightTimeByEachCarrier(List<Ticket> tickets){
        Map<String, Long> minimalFlightTime = new HashMap<>();
        for(Ticket ticket : tickets){
            long currentDuration = ticket.getFlightDurationMinutes();
            String currentCarrier = ticket.getCarrier();
            if(minimalFlightTime.containsKey(ticket.getCarrier())){
               if(minimalFlightTime.get(currentCarrier) > currentDuration){
                   minimalFlightTime.put(currentCarrier, currentDuration);
               }
            }else{
                minimalFlightTime.put(currentCarrier, currentDuration);
            }
        }
        return minimalFlightTime;
    }
    private static double getDifferenceMeanMedian(List<Ticket> tickets) {

        List<Double> sortedPrices= tickets
                .stream()
                .mapToDouble(Ticket::getPrice)
                .sorted()
                .boxed()
                .toList();

        double mean = sortedPrices.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow();

        double median;
        int size = sortedPrices.size();
        if (size % 2 == 0) {
            median = (sortedPrices.get(size / 2 - 1) + sortedPrices.get(size / 2)) / 2.0;
        } else {
            median = sortedPrices.get(size / 2);
        }

        return Math.abs(mean - median);
    }

    private static void outputResult(Map<String,Long> minimalFlightTimeByEachCarrier, double difference) {
        System.out.println("Минимальное время полета между городами Владивосток и Тель-Авив для каждого авиаперевозчика:");
        for(String carrier : minimalFlightTimeByEachCarrier.keySet()) {
            System.out.printf("- Авиаперевозчик %s - %d минут%n", carrier, minimalFlightTimeByEachCarrier.get(carrier));
        }
        System.out.println();
        System.out.printf("Разница между средней ценой и медианой для полета между городами Владивосток и Тель-Авив равна %.2f.%n%n", difference);

    }
}
