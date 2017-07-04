package org.fxapps.ml.api.provider;

import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * SPI implementation to plug new providers
 * 
 * @author wsiqueir
 *
 */
public class MLProviderFactory {

	static Logger logger = LoggerFactory.getLogger(MLProviderFactory.class);
	
	private static ServiceLoader<MLProvider> providers = ServiceLoader.load(MLProvider.class);

	static {
		for (MLProvider mlProvider : providers) {
			logger.info("Registered provider {}", mlProvider);
		}
	}

	public static ServiceLoader<MLProvider> getProviders() {
		return providers;
	}

	public static MLProvider getProvider(String providerName) {
		MLProvider provider = null;
		for (MLProvider p : providers) {
			if (p.getId().equals(providerName)) {
				provider = p;
			}
		}
		if (provider == null) {
			throw new IllegalArgumentException("Provider " + provider + " not found");
		}
		return provider;
	}
}
