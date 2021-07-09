package controller.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //idk non so più cosa mettere qui

        /* Codice va messo qui se:
         * 1. Troppo generico per essere messo in qualunque init()
         * 2. Operazione critica e fondamentale al funzionamento della webapp (una sua eccezione ne impedirà il deploy)
         * 3. Va eseguito _prima_ di qualunque init(), indistintamente dal parametro loadOnStartup
         */
    }
}
