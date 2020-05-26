@echo off

wmic cpu get LoadPercentage

wmic OS get FreePhysicalMemory

wmic ComputerSystem get TotalPhysicalMemory

wmic logicaldisk get FreeSpace

wmic logicaldisk get Size
