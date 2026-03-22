Module dependecies in an abstract way:

```
  ┌─────────────────────────────────────────────────────┐                                                                                                                       
  │                                                     │                                                                                                                       
  │  :app → :features:feed, :features:favorites,        │                                                                                                                       
  │         :features:detail                            │                                                                                                                       
  │                                                     │                                                                                                                       
  │  :features:feed ─────────┐                          │                                                                                                                       
  │  :features:favorites ────┤→ :features:core          │
  │  :features:detail ───────┘                          │                                                                                                                       
  │                          └── :data:repository       │
  │                              ├── :data:network ──┐  │                                                                                                                       
  │                              └── :data:persist ──┘  │
  │                                  ↓                  │                                                                                                                       
  │                                  :domain            │
  │                                                     │                                                                                                                       
  └─────────────────────────────────────────────────────┘
```

I chose this design to make it close to clean architecture

I made abstractions on network and persistence data sources in order for them to be swappable. In a sense, local data sources can now be swapped to preferences, file, etc.   
Also, the API for the remote data source can be swapped to another photo API easily.
                                                                                                                                                                                
I tried not to leak any flow framework to lower levels because, as we had RxJava, it came and one day was replaced by coroutines. So let's say data sources, even though      
RoomDataSource would benefit from Flow to get database update notifications, it was purposely avoided in order not to leak this API down.
                                                                                                                                                                                
From a scalability perspective, I chose to use features as modules to avoid conflicts for other developers. Clear ownership by feature. Build times are faster in the case of 
hundreds of features.
                                                                                                                                                                                
I didn't use the Pagination library from Android because it was forcing me to leak its classes down to the data source level. A custom solution was possible through
abstractions, but then it was too complex and I didn't find it beneficial.

I had help from Claude.ai




