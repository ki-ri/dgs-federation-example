/*
 * Copyright 2021 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demo.datafetchers

import com.example.demo.generated.types.Address
import com.example.demo.generated.types.Show
import com.example.demo.services.ShowsService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.beans.factory.annotation.Autowired
import com.example.demo.generated.types.Office

@DgsComponent
class ShowsDataFetcher {
    @Autowired
    lateinit var showsService : ShowsService

    val productNames = mapOf(
        "1" to "product name 1",
        "2" to "product name 2"
    )

    val addresses = mapOf(
        "1" to Address("China", "Shanghai"),
        "2" to Address("Japan", "Yokohama")
    )

    @DgsQuery
    fun shows(@InputArgument titleFilter : String?): List<Show> {
        return if(titleFilter != null) {
            showsService.shows().filter { it.title?.contains(titleFilter) == true }
        } else {
            showsService.shows()
        }
    }

    @com.netflix.graphql.dgs.DgsEntityFetcher(name = "Office")
    fun office(values: Map<String?, Any?>): Office? {
        println("values, keys: ${values.entries}")
        for (entry in values.entries) {
            println("key: ${entry.key}")
            println("value: ${entry.value}")
        }
//        return Office(values["id"] as String?, null)
        return Office(values["id"] as String?, productNames[values["id"]], addresses[values["id"]])
    }

//    @com.netflix.graphql.dgs.DgsData(parentType = "Office", field = "name")
//    fun nameFetcher(dataFetchingEnvironment: com.netflix.graphql.dgs.DgsDataFetchingEnvironment): String? {
//        val office: Office = dataFetchingEnvironment.getSource<Office>()
//        return productNames.get(office.id)
//    }
}