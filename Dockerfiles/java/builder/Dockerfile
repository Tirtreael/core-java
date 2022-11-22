
ARG REGISTRY=""
ARG NAMESPACE="ignishpc/"
ARG TAG=""
FROM ${REGISTRY}${NAMESPACE}common-builder${TAG}
ARG RELPATH=""

ENV GRADLE_VERSION=7.6-rc-1
RUN \
	mkdir -p /tmp/gradle && \
	cd /tmp/gradle && \
	wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -O src.zip && \
	unzip src && \
	mv gradle-${GRADLE_VERSION} /opt/gradle && \
	ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle && \
	cd / && \
	rm -fr /tmp/gradle

# Only x64 until the apt release
RUN cd /opt && \
	wget https://download.java.net/java/GA/jdk19.0.1/afdd2e245b014143b62ccb916125e3ce/10/GPL/openjdk-19.0.1_linux-x64_bin.tar.gz && \
	tar -xvzf openjdk-19.0.1_linux-x64_bin.tar.gz && \
	rm -fR openjdk-19.0.1_linux-x64_bin.tar.gz

ENV JAVA_HOME=/opt/jdk-19.0.1
ENV PATH=$JAVA_HOME/bin:$PATH

COPY / /tmp/core-java/
RUN mkdir ${IGNIS_HOME}/lib/java && \
	cd /tmp/core-java && \
	gradle jarAndLibs && \
	rm -rf ~/.gradle && \
	mv build/libs/* ${IGNIS_HOME}/lib/java/ && \
	cd src/main/resources/mpi4alljava && \
	export LIBRARY_PATH=${IGNIS_DEVEL}/mpi/lib/ && \
	export CPATH=${IGNIS_DEVEL}/mpi/include/ && \
	make && \
	mv libmpi4alljava.so ${IGNIS_HOME}/lib && \
	rm -fr /tmp/core-java && \
	mkdir ${IGNIS_HOME}/core-java && \
	cd ${IGNIS_HOME}/core-java && \
	MODULES=$(java --list-modules | sed -En "s/(.*)@.*/\1/p" | paste -sd "," -) && \
	jlink --output ${IGNIS_HOME}/core-java/jre --add-modules $MODULES --strip-debug --no-header-files --no-man-pages


RUN cd ${IGNIS_HOME} && \
	mkdir -p core/java && \
	cd core/java && \
	{ \
		echo '#!/bin/bash'; \
		echo 'exec ${IGNIS_HOME}/core-java/jre/bin/java --enable-preview -cp "${IGNIS_HOME}/lib/java/*" org.ignis.executor.Main "$@"'; \
	} > ${IGNIS_HOME}/bin/ignis-java && chmod +x ${IGNIS_HOME}/bin/ignis-java
	
	

COPY ${RELPATH}ignis-java-install.sh ${IGNIS_HOME}/bin
RUN chmod +x ${IGNIS_HOME}/bin/ignis-java-install.sh
