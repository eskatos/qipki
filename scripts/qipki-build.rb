#!/usr/bin/env ruby

require 'optparse'
require 'optparse/time'
require 'ostruct'
require 'pp'
require 'date'
require 'rubygems'
require 'systemu'

class BuildOptParse

    def self.parse
        # Prepare returned data structure with defaults
        options = {}
        options[:global_options] = {
            :verbose => false,
            :offline => false,
            :skip_tests => false
        }
        options[:command] = nil
        options[:command_options] = {}
        commands = {
            'clean' => OptionParser.new do |opts|
            end,
            'install' => OptionParser.new do |opts|
            end,
            'web' => OptionParser.new do |opts|
            end,
            'site' => OptionParser.new do |opts|
            end,
        }
        global = OptionParser.new do |opts|
            opts.banner = "Usage: qipki-build [global-options] command [command-options]"
            opts.separator ""
            opts.separator "Global options:"
            opts.on("-v", "--[no-]verbose", "Run verbosely") do |v|
                options[:global_options][:verbose] = v
            end
            opts.on("-o", "--[no-]offline", "Run offline") do |o|
                options[:global_options][:offline] = o
            end
            opts.on("--[no-]skiptests", "Skip tests") do |o|
                options[:global_options][:skip_tests] = o
            end
            opts.on_tail("-h", "--help", "Show this message") do
                puts opts
                puts_help_bottom commands
                exit 0
            end
            opts.on_tail("--version", "Show version") do
                puts `echo "Version: $(cat \$QIPKI_BASEDIR/pom.xml | grep "<version>.*</version>" | head -n 2 | tail -n 1 | sed -e "s\/^.*>\\(.*\\)<.*$\/\\1\/i")"`
                exit
            end
        end
        global.order!
        options[:command] = ARGV.shift
        if commands[options[:command]].nil?
            puts global
            puts_help_bottom commands
            exit 1
        end
        commands[options[:command]].order!
        options
    end

    :private
    def self.puts_help_bottom commands
        puts
        puts "Available commands: #{commands.keys.join(", ")}"
        puts "    Type 'qipki-build [command] --help' to get help on each command."
        puts
    end
    
end

build_options = BuildOptParse.parse

puts
pp "Global options: ", build_options[:global_options].to_s
puts
puts "Command: " + build_options[:command]
puts
pp "Command options: ", build_options[:command_options].to_s
puts

############################################################
# From here options are parsed and we will issue commands. #
############################################################

module System
    def execute(cmd, verbose=false)
        debug("Running: #{cmd}")
        status, stdout, stderr = systemu cmd do |child_pid|
            while true do
                sleep 1
                STDERR.print "."
                STDERR.flush
            end
        end
        STDERR.puts
        if (verbose or status.exitstatus != 0)
            info("Process exited successfully") if status.exitstatus == 0
            error("Process exited with error: #{status.exitstatus}") if status.exitstatus != 0
            if stdout && stdout.length > 0
                stdout.each do |line|
                    line.strip!
                    info("OUT: #{line}") unless line.empty?
                end
            end
            if stderr && stderr.length > 0
                stderr.each do |line|
                    line.strip!
                    warn("ERR: #{line}") unless line.empty?
                end
            end
            raise "Process exited with error: #{status.exitstatus}" if status.exitstatus != 0
        end
    end
    def error(msg)
        puts build('ERROR', msg, RED)
    end
    def warn(msg)
        puts build('WARNING', msg, YELLOW)
    end
    def info(msg)
        puts build('INFO', msg, WHITE)
    end
    def debug(msg)
        puts build('DEBUG', msg, GRAY)
    end
    def input(msg)
        print build('INPUT', msg, GREEN)
        gets.chomp
    end
    :private
    RED           = "\033[1;31;40m"
    GREEN         = "\033[1;32;40m"
    YELLOW        = "\033[1;33;40m"
    WHITE         = "\033[1;37;40m"
    GRAY          = "\033[1;30;40m"
    LEVEL_MAX_LEN = 7
    def build(level, msg, color_code)
        '[ ' + colorize(level[0..(LEVEL_MAX_LEN-1)].center(LEVEL_MAX_LEN), color_code) + ' ] ' + colorize(msg, color_code)
        #DateTime.now.to_s + ' [ ' + colorize(level[0..(LEVEL_MAX_LEN-1)].center(LEVEL_MAX_LEN), color_code) + ' ] ' + colorize(msg, color_code)
    end
    def colorize(msg, color_code)
        "#{color_code}#{msg}\033[0m"
    end
end

include System

mvn_command = "mvn -f \"#{ENV['QIPKI_BASEDIR']}/pom.xml\""
mvn_options = " -e" if build_options[:global_options][:verbose]
mvn_options << " -o" if build_options[:global_options][:offline]
mvn_options << " -DskipTests" if build_options[:global_options][:skip_tests]

begin
    case build_options[:command]
    when 'clean'
        
        mvn_command << mvn_options
        mvn_command << " clean"
        execute mvn_command, true
        
    when 'install'
        
        mvn_command << mvn_options
        mvn_command << " clean install"
        execute mvn_command, true
        
    when 'web'
        
        mvn_command = "mvn -f \"#{ENV['QIPKI_BASEDIR']}/web-client/pom.xml\""
        mvn_command << mvn_options
        mvn_command << " gwt:run"
        execute mvn_command, true
        
    when 'site'

        clean = mvn_command.clone
        clean << mvn_options
        clean << " clean"
        execute clean, true
    
        root_install = mvn_command.clone
        root_install << mvn_options
        root_install << " -N install"
        execute root_install, true
    
        site = mvn_command.clone
        site << mvn_options
        site << " site"
        execute site, true
        
        site_stage = mvn_command.clone
        site_stage << mvn_options
        site_stage << " site:stage -DstagingDirectory=\"#{ENV['QIPKI_BASEDIR']}/target/qipki-site-www/\""
        execute site_stage, true
    
    end
rescue
    error "Something wrong happened during build execution, see logs for details."
    exit 1
end

