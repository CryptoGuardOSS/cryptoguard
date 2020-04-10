FROM openjdk:11.0.3-jdk

RUN apt-get update

RUN yes|apt-get install zip
#RUN yes|apt-get install snapd
#RUN snap install android-studio --classic
RUN apt-get install -y python3-pip
RUN apt-get update

# add requirements.txt, written this way to gracefully ignore a missing file
COPY . .
RUN ([ -f requirements.txt ] \
    && pip3 install --no-cache-dir -r requirements.txt) \
        || pip3 install --no-cache-dir jupyter jupyterlab

USER root

# Download the kernel release
RUN curl -L https://github.com/SpencerPark/IJava/releases/download/v1.3.0/ijava-1.3.0.zip > ijava-kernel.zip

# Unpack and install the kernel
RUN unzip ijava-kernel.zip -d ijava-kernel \
  && cd ijava-kernel \
  && python3 install.py --sys-prefix


# Set up the user environment

ENV NB_USER runner
ENV NB_UID 1000
ENV HOME /home/$NB_USER

RUN adduser --disabled-password \
    --gecos "Default user" \
    --uid $NB_UID \
    $NB_USER

COPY . $HOME
RUN chown -R $NB_UID $HOME

USER $NB_USER

# Installing SDK Man
RUN curl -s "https://get.sdkman.io" | bash
#RUN apt-get update
RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh"
#RUN /bin/bash -c "yes|sdk install java 6.0.242-zulu"
#RUN /bin/bash -c "yes|sdk install java 7.0.242-zulu"

# Installing Java and Maven, removing some unnecessary SDKMAN files 
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && \
    yes | sdk install java 7.0.242-zulu && \
    yes | sdk install java 8.0.242-zulu && \
    yes | sdk install gradle 4.10.3 && \
    yes | sdk install jbang 0.8.1 && \
    rm -rf $HOME/.sdkman/archives/* && \
    rm -rf $HOME/.sdkman/tmp/*"

## TODO! need to install android sdk
#RUN mkdir android-sdk
#WORKDIR android-sdk
#RUN wget -q https://dl.google.com/android/repository/tools_r25.2.5-linux.zip && \
#    unzip -qq tools_r25.2.5-linux.zip && \
#    rm tools_r25.2.5-linux.zip && \
#    echo y | tools/bin/sdkmanager "platforms;android-28"
#&& ${'\\'}
#    echo y | tools/bin/sdkmanager "build-tools;25.0.2" && ${'\\'}
#    echo y | tools/bin/sdkmanager "extras;android;m2repository" && ${'\\'}
#    echo y | tools/bin/sdkmanager "extras;google;google_play_services" && ${'\\'}
#    echo y | tools/bin/sdkmanager "extras;google;m2repository" && ${'\\'}
#    echo y | tools/bin/sdkmanager "patcher;v4" && ${'\\'}
#    echo y | tools/bin/sdkmanager "platform-tools"
#ENV ANDROID_HOME "/android-sdk"

#Issue at snap install android stuido
#RUN bash -c "systemctl restart snapd"
#RUN systemctl status snapd
#RUN bash -c "/usr/bin/snap install android-studio --classic"
#RUN bash -c "snap install androidsdk"
#RUN bash -c "androidsdk 'platforms;android-28'"

# Launch the notebook server
WORKDIR $HOME
CMD ["jupyter", "notebook", "--ip", "0.0.0.0"]
