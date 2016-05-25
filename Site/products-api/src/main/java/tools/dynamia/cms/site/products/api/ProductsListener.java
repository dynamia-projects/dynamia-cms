/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.products.api;

/**
 * This interface is used by remote datasources to notify DynamiaCMS that some product or category as changed. ProductsListeners' implementation should be
 * running in the CMS side.
 *
 * @author Mario Serrano Leones
 */
public interface ProductsListener {

    public void productChanged(DataChangedEvent evt);

    public void categoryChanged(DataChangedEvent evt);

    public void brandChanged(DataChangedEvent evt);
    
    public void storeChanged(DataChangedEvent evt);
}
