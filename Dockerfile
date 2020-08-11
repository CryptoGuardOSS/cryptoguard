FROM openjdk:11.0.7-jdk

RUN apt-get update
RUN apt update

RUN yes|apt-get install zip
RUN apt-get install -y python3-pip
RUN apt-get update

# Downloading the Android Library
RUN cd /opt && \
	wget --output-document=android-sdk.zip --quiet https://dl.google.com/android/repository/android-22_r02.zip && \
	unzip android-sdk.zip && \
	rm -f android-sdk.zip && \
	mv android-5.1.1 android && \
	chown -R 777 android

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
##Trying to dynamically retrieve the latest 8.0 jdk
#RUN bash -c "source /home/runner/.sdkman/bin/sdkman-init.sh && \
#    yes|sdk install java $(sdk ls java|grep zulu|grep 8.0|head -n 1|awk -F ' ' '{print $8}') && \
#    yes|sdk install java $(sdk ls java|grep zulu|grep 7.0|head -n 1|awk -F ' ' '{print $8}')"
   

# Installing Java and Maven, removing some unnecessary SDKMAN files 
RUN bash -c "source /home/runner/.sdkman/bin/sdkman-init.sh && \
    yes | sdk install java 7.0.262-zulu && \
    yes | sdk install java 8.0.265-zulu && \
    yes | sdk install gradle 6.0 && \
    yes | sdk install java 11.0.8.hs-adpt && \
    sdk default java 11.0.8.hs-adpt"

USER root

RUN mkdir -p /home/runner/.sdkman/candidates/android/22_r02
RUN mv /opt/android /home/runner/.sdkman/candidates/android/22_r02/platforms
RUN ln -s /home/runner/.sdkman/candidates/android/22_r02/platforms /home/runner/.sdkman/candidates/android/current

RUN chmod 777 -R /home/runner/.sdkman

USER $NB_USER

RUN bash -c "echo \"jupyter lab --ip=0.0.0.0\">>/home/runner/.bash_aliases"

# Launch the notebook server
WORKDIR $HOME
CMD ["jupyter", "lab", "--ip", "0.0.0.0"]
