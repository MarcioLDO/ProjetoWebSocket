package com.lino.projetowebsocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/url")
public class LinoWebSocket {
    private static Set<Session> SESSOES = Collections.synchronizedSet(new HashSet<Session>());

    static {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(5000);
                        } catch (Exception e) {
                        }
                        broadcast("Vsf mulekessss");
                    }
                }
            });
            thread.start();
        } catch (Exception e) {
        }
    }

    @OnMessage
    public String onMessage(String message) {
        return "Retorno do onMessage ";
    }

    @OnOpen
    public void onOpen(Session peer) {
        SESSOES.add(peer);
    }

    @OnClose
    public void onClose(Session peer) {
        SESSOES.remove(peer);
    }

    public static void listarSessoes() {
        Iterator<Session> iteratorSession = SESSOES.iterator();
        Session session;
        while (iteratorSession.hasNext()) {
            session = iteratorSession.next();
            System.out.println("Sessão: " + session.getId());
        }
    }

    public static void broadcast(final String mensagem) {
        SESSOES.stream().filter(new Predicate<Session>() {
            @Override
            public boolean test(Session session) {
                return session.isOpen();
            }
        }).forEach(new Consumer<Session>() {
            @Override
            public void accept(Session session) {
                try {
                    session.getBasicRemote().sendText("Servidor: " + mensagem);
                } catch (Exception e) {
                }
            }
        });
    }
}
