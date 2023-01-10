package org.csanchez.jenkins.plugins.kubernetes.pipeline;

import static org.csanchez.jenkins.plugins.kubernetes.KubernetesTestUtil.assumeKubernetes;

import org.csanchez.jenkins.plugins.kubernetes.KubernetesTestUtil;
import org.csanchez.jenkins.plugins.kubernetes.pipeline.steps.CreateWorkflowJobThenScheduleRun;
import org.csanchez.jenkins.plugins.kubernetes.pipeline.steps.RunId;
import org.csanchez.jenkins.plugins.kubernetes.pipeline.steps.SetupCloud;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.jvnet.hudson.test.RealJenkinsRule;

public abstract class AbstractKubernetesPipelineRJRTest {

    @Rule
    public TestName name = new TestName();

    @Rule
    public RealJenkinsRule rjr = new RealJenkinsRule();

    protected RunId runId;

    private SetupCloud setup;

    public AbstractKubernetesPipelineRJRTest(SetupCloud setup) {
        this.setup = setup;
    }

    @BeforeClass
    public static void isKubernetesConfigured() throws Exception {
        assumeKubernetes();
    }

    @Before
    public void setUp() throws Throwable {
        rjr.startJenkins();
        rjr.runRemotely(setup);
        runId = rjr.runRemotely(new CreateWorkflowJobThenScheduleRun(KubernetesTestUtil.loadPipelineScript(getClass(), name.getMethodName() + ".groovy")));
    }
}