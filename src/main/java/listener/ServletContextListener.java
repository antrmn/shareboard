package listener;

import persistence.ConPool;
import section.Section;
import section.SectionDAO;
import section.SectionSpecificationBuilder;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try(Connection con = ConPool.getConnection()){
            SectionDAO service = new SectionDAO(con);
            List<Section> _sections = service.fetch(new SectionSpecificationBuilder().sortById().build());
            ConcurrentMap<Integer, Section> sections
                    = _sections.stream().collect(Collectors.toConcurrentMap(x -> x.getId(), x -> x));
            sce.getServletContext().setAttribute("sections", sections);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Impossibile caricare le sezioni nel servletContext");
        }
    }
}
