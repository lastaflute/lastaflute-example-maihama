/*
 * Copyright 2015-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage.unit;

import org.dbflute.utflute.lastaflute.WebContainerTestCase;
import org.lastaflute.job.LaJob;
import org.lastaflute.job.mock.MockJobRuntime;

/**
 * Use like this:
 * <pre>
 * YourTest extends {@link UnitOrleansJobTestCase} {
 * 
 *     public void test_yourMethod() {
 *         <span style="color: #3F7E5E">// ## Arrange ##</span>
 *         YourJob job = new YourJob();
 *         <span style="color: #FD4747">inject</span>(job);
 *         MockJobRuntime runtime = mockRuntime(job);
 * 
 *         <span style="color: #3F7E5E">// ## Act ##</span>
 *         job.run(runtime);
 * 
 *         <span style="color: #3F7E5E">// ## Assert ##</span>
 *         assertTrue(runtime...);
 *     }
 * }
 * </pre>
 * @author jflute
 */
public abstract class UnitOrleansJobTestCase extends WebContainerTestCase {

    /**
     * Prepare mock of job runtime.
     * @param job The execution job on the runtime. (NotNull)
     * @return The mock instance of job runtime for the job. (NotNull) 
     */
    protected MockJobRuntime mockRuntime(LaJob job) {
        return MockJobRuntime.of(job.getClass());
    }
}
