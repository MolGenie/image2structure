package com.molgenie.i2s;

import com.molgenie.i2s.config.*;
import com.molgenie.i2s.models.*;
import com.molgenie.i2s.models.common.ClassificationOutcome;
import com.molgenie.i2s.models.common.ErrorResponse;
import com.molgenie.i2s.models.common.OsrResult;
import com.molgenie.i2s.models.common.ScoreOutcome;
import com.molgenie.i2s.services.IOsrService;
import com.molgenie.i2s.services.IImageClassificationService;
import io.javalin.Javalin;
import org.int4.dirk.api.InstanceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		var settings = AppPropertiesLoader.load();
		var resolver = DiBuilder.build(settings);

		if (args.length > 0) {
			// run and quit after processing the command line arguments
			executeCommand(args, resolver);
		} else {
			// run api server and wait for requests to come in
			log.info("Starting up server...");
			var app = Api.create(settings);
			configureEndpoints(app, resolver, settings);
			app.start();
		}
	}

	private static void executeCommand(String[] args, InstanceResolver resolver) {
		// When running as command line app, adjust logging
		System.setProperty("logging.file.name", ""); // Disable file logging
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		root.setLevel(ch.qos.logback.classic.Level.ERROR);
		
		switch (args[0]) {
			case "-h", "--help" -> {
				System.out.println("Usage: java -jar i2s.jar [options]");
				System.out.println("Options:");
				System.out.println("  -h, --help     Show this help message");
				System.out.println("  -i <filename> Process image file using OSR service");
				System.out.println("  <NO OPTIONS>   Run the API server and wait for requests");
				System.exit(0);
			}
			case "-i" -> {
				if (args.length < 2) {
					System.err.println("Error: Missing filename for -in option");
					System.err.println("Usage: java -jar i2s.jar -i <filename>");
					System.exit(1);
				}
				var filename = args[1];
				try {
					var service = resolver.getInstance(IOsrService.class);
					var request = new OsrImageRequest();
					request.setImageFilename(filename);
					var response = service.performOsr(request);
				} catch (Exception e) {
					log.error("Error processing image file: " + filename, e);
					System.err.println("Error processing image file: " + e.getMessage());
					System.exit(1);
				}
				System.exit(0);
			}
			default -> {
				System.err.println("Error: Unknown option: " + args[0]);
				System.err.println("Use -h or --help for usage information");
				System.exit(1);
			}
		}
	}

	private static void configureEndpoints(Javalin app, InstanceResolver resolver, IApiSettings settings) {
		// API endpoints
		app.post(settings.baseApiPath() + "/classifyImage", ctx -> {
			var payload = ctx.bodyAsClass(ClassifyImageRequest.class);
			if ((payload.getImageContent() == null && payload.getImageFilename() == null) || (payload.getImageContent() != null && payload.getImageFilename() != null)) {
				ctx.status(400);
				ctx.json(new ErrorResponse("Either imageContent or imageFilename must be specified, but not both", 400, payload.getImageFilename()));
				return;
			}
			var service = resolver.getInstance(IImageClassificationService.class);
			var response = service.classifyImage(payload);
			ctx.json(response);
		});
		app.post(settings.baseApiPath() + "/performOSR", ctx -> {
			var payload = ctx.bodyAsClass(OsrImageRequest.class);
			var service = resolver.getInstance(IOsrService.class);
			var result = service.performOsr(payload);
			//System.out.println("result:"+result);
			ctx.json(result);
		});
		app.post(settings.baseApiPath() + "/processImage", ctx -> {
			var payload = ctx.bodyAsClass(ProcessImageRequest.class);
			var result = new ProcessImageResponse(
					new ClassificationOutcome(
							new String[]{"label"},
							new ScoreOutcome[]{new ScoreOutcome(new String[]{"score"})}),
					new OsrResult("Not Implemented", null, null, "Not Implemented"));
			ctx.json(result);
		});
	}
}
