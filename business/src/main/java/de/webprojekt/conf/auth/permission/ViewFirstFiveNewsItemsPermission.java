package de.webprojekt.conf.auth.permission;

import java.util.List;

import de.webprojekt.models.Todo;
import org.apache.shiro.authz.Permission;

public class ViewFirstFiveNewsItemsPermission implements Permission {

    private final List<Todo> news;

    public ViewFirstFiveNewsItemsPermission(final List<Todo> news) {
        this.news = news;
    }

    @Override
    public boolean implies(Permission p) {
        return false;
    }

    public boolean check() {
        return this.news.size() < 5;
    }
}
