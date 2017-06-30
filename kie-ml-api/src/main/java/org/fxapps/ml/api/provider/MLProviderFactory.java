package org.fxapps.ml.api.provider;

import java.util.ServiceLoader;

/**
 * 
 * SPI implementation to plug new providers
 * 
 * @author wsiqueir
 *
 */
public class MLProviderFactory {

	private static ServiceLoader<MLProvider> providers = ServiceLoader.load(MLProvider.class);
	
	static {
		try {
			registerProviders();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void registerProviders() throws Exception {
	}

	public static ServiceLoader<MLProvider> getProviders() {
		return providers;
	}

	public static MLProvider getProvider(String providerName) {
		MLProvider provider = null;
		for (MLProvider p : providers) {
			if(p.getId().equals(providerName)){
				provider = p;
			}
		}
		if(provider == null) {
			 throw new IllegalArgumentException("Provider " + provider + " not found");
		}
		return provider;
	}
}
