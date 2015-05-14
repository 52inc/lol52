package com.ftinc.lolserv.api;

import dagger.Subcomponent;

/**
 * Created by r0adkll on 5/14/15.
 */
@Subcomponent(modules = ApiModule.class)
public interface ApiComponent {
    void inject(API api);
}
