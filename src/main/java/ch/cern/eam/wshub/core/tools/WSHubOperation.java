package ch.cern.eam.wshub.core.tools;

import ch.cern.eam.wshub.core.client.EAMContext;

@FunctionalInterface
public interface WSHubOperation<A, R> {
    R apply(EAMContext eamContext, A a) throws EAMException;
}