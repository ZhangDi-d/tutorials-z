package org.example.maven.plugin.validator;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Repository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * @author dizhang
 * @date 2021-03-16
 */
@Mojo(defaultPhase = LifecyclePhase.COMPILE, name = "dependency-counter")
public class DependencyCounterMojo extends AbstractMojo {

    @Parameter(property = "scope")
    private String scope;

    @Parameter(readonly = true, required = true, defaultValue = "${project}")
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        List<Dependency> dependencies = project.getDependencies();
        long count = dependencies.stream().filter(dependency -> (scope == null || scope.isEmpty() || dependency.getScope().equals(scope))).count();
        List<Repository> repositories = project.getRepositories();
        getLog().info("count of dependencies: " + count + " , count of repositories:" + repositories.size());

    }
}
