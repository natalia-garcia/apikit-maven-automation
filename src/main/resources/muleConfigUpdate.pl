#!/usr/bin/env perl

use strict;
use warnings;

my $content;
my $snippet = <<EOD;
    <cors:config name="Cors_Configuration" doc:name="Cors Configuration">
        <cors:origins>
            <cors:origin url="http://ec2-54-69-7-25.us-west-2.compute.amazonaws.com:9000/">
                <cors:methods>
                    <cors:method>POST</cors:method>
                    <cors:method>PUT</cors:method>
                    <cors:method>DELETE</cors:method>
                    <cors:method>GET</cors:method>
                </cors:methods>
                <cors:headers>
                    <cors:header>content-type</cors:header>
                </cors:headers>
            </cors:origin>
            <cors:origin url="http://localhost:9090">
                <cors:methods>
                    <cors:method>POST</cors:method>
                    <cors:method>PUT</cors:method>
                    <cors:method>DELETE</cors:method>
                    <cors:method>GET</cors:method>
                </cors:methods>
                <cors:headers>
                    <cors:header>content-type</cors:header>
                </cors:headers>
            </cors:origin>
        </cors:origins>
    </cors:config>
    <flow name="main" doc:name="main">
        <http:inbound-endpoint doc:name="HTTP" exchange-pattern="request-response" host="localhost" path="api" port="9091">
            <api-platform-gw:register-as apikit-ref="apiConfig" />
        </http:inbound-endpoint>
        <cors:validate config-ref="Cors_Configuration" publicResource="false" acceptsCredentials="false" doc:name="CORS Validate" />
        <apikit:router config-ref="apiConfig" doc:name="APIkit Router" />
        <exception-strategy ref="apiKitGlobalExceptionMapping" doc:name="Reference Exception Strategy" />
    </flow>
    <flow name="consoletestingFlow1" doc:name="consoletestingFlow1">
        <http:inbound-endpoint exchange-pattern="request-response" host="localhost" port="9090" path="console" doc:name="HTTP" />
        <apikit:console config-ref="apiConfig" doc:name="APIkit Console" />
    </flow>
EOD

{ local $/; $content = <>; }

$content =~ s/<flow name="api-main">.*<\/flow>(\s*<flow name="put:\/items\/\{itemId\}:application\/json:apiConfig">)/$snippet$1/s;
print $content;

