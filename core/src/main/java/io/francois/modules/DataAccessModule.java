package io.francois.modules;

import io.francois.dataAccess.RepositoryAggregator;
import io.francois.dataAccess.RgGatewayRepositoryImpl;
import io.francois.dataAccess.interfaces.RgGatewayRepository;
import com.google.inject.AbstractModule;

public class DataAccessModule extends AbstractModule {

    @Override protected void configure() {
        bind(RgGatewayRepository.class).to(RgGatewayRepositoryImpl.class);

        // self bind
        bind(RepositoryAggregator.class);
    }
}


