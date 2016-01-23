#!/bin/bash

startupEmulatorIfNotRunning () {
  local targets=("${!1}")
  echo ""
  echo "*** Starting emulators for [${targets[@]}] ***"
  echo ""

  for target in "${targets[@]}"
  do :
    # TODO: check if emulator is running already
    echo "Starting emulator for [$target]"
    emulator -avd "$target" &
  done

  local running=$(adb devices | grep 'emulator' | grep 'device' | wc -l)

  while [ "$running" -lt "4" ]
  do
    echo "Waiting for 4 running emulators. Currently running are [$running]"
    sleep 2
    running=$(adb devices | grep 'emulator' | grep 'device' | wc -l)
  done

  echo "Starting up emulators finished."
  echo ""
}

verifyAVDsExist () {
  local targets=("${!1}")
  echo ""
  echo "*** Verifying avd for [${targets[@]}] exists ***"
  echo ""

  for target in "${targets[@]}"
  do :
    if (android list avd | grep -q "$target")
    then
      echo "AVD [$target] exists. Continuing"
    else
      echo "AVD [$target] does not exist. Creating it now..."
      echo no | android create avd --force -n "$target" -t "$target" --abi x86 --skin WXGA720
    fi
  done
  echo "Verifying avd existance finished."
  echo ""
}

bootAndUnlockEmulator() {
  echo ""
  echo "*** Waiting for all emulators to start up... ***"
  echo ""

  local emulators=($(adb devices | grep 'emulator' | cut -f1))
  echo "Found running emulators [${emulators[@]}]"

  for emulator in "${emulators[@]}"
  do :
    echo "waiting for emulator [$emulator] to boot up..."
    adb -s "$emulator" wait-for-device
    local bootCompleted=$(adb -s "$emulator" shell getprop sys.boot_completed | tr -d '\r')

    while [ "$bootCompleted" != "1" ]; do
      sleep 2
      bootCompleted=$(adb -s "$emulator" shell getprop sys.boot_completed | tr -d '\r')
    done

    echo "Done. Unlocking the emulator"
    adb -s "$emulator" shell input keyevent 82
  done

  echo "Waiting for emulator startup finished."
  echo ""
}

verifyEmulatorsRunning () {
  echo "***********************************"
  echo "*** Verifying emulators running ***"
  echo "***********************************"

  local targets=("android-19" "android-21" "android-22" "android-23")

  verifyAVDsExist targets[@]

  startupEmulatorIfNotRunning targets[@]

  bootAndUnlockEmulator

}

runTests() {
  echo "*********************"
  echo "*** Running tests ***"
  echo "*********************"

  echo ""

  gradle spoon
}

shutdownEmulators() {
  echo "**************************"
  echo "*** Shutdown emulators ***"
  echo "**************************"

  echo ""

  local emulators=($(adb devices | grep '-' | cut -f1))
  echo "Found running emulators [${emulators[@]}]"

  for emulator in "${emulators[@]}"
  do :
    echo "Stopping emulator [$emulator]"
    adb -s "$emulator" emu kill
  done

  echo "Waiting for emulator startup finished."
  echo ""
}

processTestReport() {
  echo "***************************"
  echo "*** Process test report ***"
  echo "***************************"

  echo ""

  # switch to gh-pages branch
  git checkout gh-pages
  git pull

  # copy test report into specific directory
  local buildcount=$(ls builds | wc -l)
  ((buildcount+=1))
  echo "Processing spoon report for build [$buildcount]"

  cp -r app/build/spoon/debug "builds/$buildcount"

  # link test report from index.html
  local reportContent=$(sed -n "/<body>/,/<\/body>/p" "builds/$buildcount/index.html" | sed '1d;$d')
  local reportHeader="       <h4>Build $buildcount</h4>"
  local placeHolder="<span id='placeholder'/>"
  local index=$(<index.html)
  echo -e "${index/$placeHolder/$placeHolder \\n\\n $reportHeader \\n $reportContent}" > index.html

  # fix links to tests
  sed -i "s/href=\"device/href=\"builds\/$buildcount\/device/g" index.html

  # commit and push test report
  git add "builds/$buildcount"
  git commit -m"Added builreport for build $buildcount"
  git push

  git checkout -
}

main () {
  echo "**********************"
  echo "*** Starting Build ***"
  echo "**********************"

  echo ""

  #verifyEmulatorsRunning

  #runTests

  #shutdownEmulators

  processTestReport
}

main $*