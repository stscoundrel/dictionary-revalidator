# Dictionary Revalidator

Kotlin / Spring Boot API for triggering revalidation actions in various dictionary websites using incremental static generation.

Abstracts a few  apikey / secret guarded endpoints in various dictionary projects under the same umbrella, allowing revalidation of pages using one API only.

## Why though

This is essentially just a web service version of existing revalidation scripts shipped with projects like [Old Norse Dictionary](https://github.com/stscoundrel/cleasby-vigfusson-next), [Old Icelandic Dictionary](https://github.com/stscoundrel/old-icelandic-zoega-next), [Old Norwegian Dictionary](https://github.com/stscoundrel/old-norwegian-dictionary-next) and [Old Swedish Dictionary](https://github.com/stscoundrel/old-swedish-dictionary-next).

Each of those sites contains too many statically generated pages to be deployed in one go to Vercel, as they hit Vercels per-build file limit. The solution has been to deploy them with only few core pages and use ISR to generate the pages as they are accessed for the first time. However, this first load may be bit too slow for casual browser and it actually consumes more resources than triggering API endpoint based revalidation. Considering that these sites are all hosted on free tiers, saving Vercels resources is both polite and required. Therefore, whenever new changes are deployed, new revalidation for tens of thousands of pages is needed.
