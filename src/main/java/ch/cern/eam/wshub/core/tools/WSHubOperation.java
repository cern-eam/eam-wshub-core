package ch.cern.eam.wshub.core.tools;

import ch.cern.eam.wshub.core.client.InforContext;

@FunctionalInterface
public interface WSHubOperation<A, R> {
    R apply(InforContext inforContext, A a) throws InforException;
}