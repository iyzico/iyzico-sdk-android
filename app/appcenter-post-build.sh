#!/usr/bin/env bash
if [ "$AGENT_JOBSTATUS" != "Succeeded" ]; then
	    curl -d '{
      "username":"Release Bot",
      "embeds":[{"description": " FAILLLLL!!!! \n ------------ \nApplication Name: '"${APPLICATON_NAME}"' \nRelease Type '"${RELEASE_TYPE}"' \nBranch: '"${APPCENTER_BRANCH}"' \nBuildNumber: '"${APPCENTER_BUILD_ID}"'\nBuildType: '"${APPCENTER_ANDROID_VARIANT}"'\n"}]
      }' -H "Content-Type: application/json" -X POST https://discord.com/api/webhooks/808404678584041486/EvRFSkH2czPSsaSfhub4Y1sNVGrVZLgGR-kSRB1lPm58g-UiZigDPcKGC8bS9kT0VAuI
	else
	  curl -d '{
      "username":"Release Bot",
            "embeds":[{"description": "Application Name: '"${APPLICATON_NAME}"' \nRelease Type '"${RELEASE_TYPE}"' \nBranch: '"${APPCENTER_BRANCH}"' \nBuildNumber: '"${APPCENTER_BUILD_ID}"'\nBuildType: '"${APPCENTER_ANDROID_VARIANT}"'\n"}]
      }' -H "Content-Type: application/json" -X POST https://discord.com/api/webhooks/808404678584041486/EvRFSkH2czPSsaSfhub4Y1sNVGrVZLgGR-kSRB1lPm58g-UiZigDPcKGC8bS9kT0VAuI
	fi




