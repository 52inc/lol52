# lol52
lolommits server that acts as a webhook, stores the gifs, then offers an extensible API for retrieving the data/gifs.

The goal of this server is to be extensible and dynamic to receive an incoming lolcommits
`POST` request from a developers machine.

This server will provide the following http routes:

 - **POST** `http://example.com/lolcommits/webhook`  
   This is the endpoint that you configure the lolcommits plugin `uploldz` to point to, and it will send a post reqeust of the associated media file + metadata. From here it is distributed via the `Nexus` class through the RxBus, reactive extensions event bus i.e.; [RxJava](https://github.com/reactivex/rxjava), where it is ran through all the plugins.
   
 - **GET** `http://example.com/
