package parser;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class PostgresWriter {
    private SessionFactory sessionFactory;
    private AtomicInteger counter = new AtomicInteger();

    private void openSessionFactory() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    private void closeSessionFactory() {
        sessionFactory.close();
    }

    public void writeLocations(Path path, Class<? extends Location> locationClass, BiFunction<Path, Class<? extends Location>, List<Route>> parser) {
        try {
            openSessionFactory();
            Files.walk(path).filter(p -> !Files.isDirectory(p)).forEach(file -> {
                System.out.println("Parsing data from: " + file.toString());
                List<Route> routes = parser.apply(file, locationClass);
                System.out.println("Writing data to database");
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                routes.stream()
                        .map(route -> route.getLocations())
                        .flatMap(List::stream)
                        .forEach(location -> {
                            session.save(location);
                            if(counter.addAndGet(1) % 1000 == 0) {
                                session.flush();
                                session.clear();
                            }
                        });
                session.getTransaction().commit();
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSessionFactory();
        }
    }
}
