FROM gitpod/workspace-postgres:commit-507a50aeb98b86396d8d3b705e1ed651ed0af6fe

USER gitpod

RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh \
             && sdk install java 16.0.1.hs-adpt"
