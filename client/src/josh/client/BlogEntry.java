package josh.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class BlogEntry implements EntryPoint {
    public void onModuleLoad() {
      new josh.client.BlogApp().interact(RootPanel.get("kmeans"));
    }
}
