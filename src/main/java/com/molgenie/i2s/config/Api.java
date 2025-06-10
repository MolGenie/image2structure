package com.molgenie.i2s.config;

import com.molgenie.i2s.models.common.ErrorResponse;
import com.molgenie.i2s.services.ProcessingErrorException;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public final class Api {
	
	public static Javalin create(IJettySettings settings) {
		// Configure Jetty ThreadPool
		var app = Javalin.create(config -> {
			config.server(() -> createJettyServer(settings));
			var objectMapper = com.fasterxml.jackson.databind.json.JsonMapper.builder()
					.serializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
					.build();
			config.jsonMapper(new JavalinJackson(objectMapper));
		});

		// Global exception handler
		app.exception(ProcessingErrorException.class, (e, ctx) -> {
			ctx.status(500);
			ctx.json(new ErrorResponse(e.getMessage(), e.getErrorCode(), e.getFileName()));
		});
		app.exception(RuntimeException.class, (e, ctx) -> {
			ctx.status(500);
			ctx.json(new ErrorResponse(e.getMessage(), 500, null));
		});
		return app;
	}
	
	private static Server createJettyServer(IJettySettings settings) {
		// Configure Jetty ThreadPool
		QueuedThreadPool threadPool = new QueuedThreadPool(settings.maxThreadCount());
		threadPool.setName("server-thread-pool");

		// Create and configure Jetty server
		Server server = new Server(threadPool);
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(settings.listeningPort());
		server.addConnector(connector);
		return server;
	}
}
