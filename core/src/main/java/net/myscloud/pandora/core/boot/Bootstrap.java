package net.myscloud.pandora.core.boot;

/**
 * Created by user on 2015/7/2.
 */
public interface Bootstrap {
    void start();

    void stop();

    void restart();

    void status();

    Bootstrap init();

    Bootstrap bind(int port);
}
