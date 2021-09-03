FROM gitpod/workspace-postgres

USER gitpod

RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh \
             && sdk install java 16.0.1.hs-adpt"