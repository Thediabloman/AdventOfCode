curl -Lo coursier https://git.io/coursier-cli
chmod +x coursier
./coursier bootstrap org.scalameta:scalafmt-cli_2.12:2.3.2 \
  -r sonatype:snapshots \
  -o scalafmt --main org.scalafmt.cli.Cli
rm -f coursier
./scalafmt --version # should be 2.3.2
