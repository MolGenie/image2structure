package com.molgenie.i2s.config;

import com.molgenie.i2s.services.FakeImageClassificationService;
import com.molgenie.i2s.services.OsraCommandService;
import org.int4.dirk.api.InstanceResolver;
import org.int4.dirk.di.Injectors;

public final class DiBuilder {
	public static InstanceResolver build(AppProperties settings) {
		var injector = Injectors.autoDiscovering();
		injector.registerInstance(settings);
		injector.register(FakeImageClassificationService.class);
		injector.register(OsraCommandService.class);
		return injector;
	}
}