@echo off
echo Building all projects...

rem Создаем папку для APK если её нет
if not exist "release_apks" mkdir release_apks
echo Created directory for APK files

echo.
echo Building kwk-1...
call gradlew :kwk-1:assembleRelease
if errorlevel 1 goto error
copy /Y "kwk-1\build\outputs\apk\release\*.apk" "release_apks\"
if errorlevel 1 goto copy_error

echo.
echo Building kwk-2...
call gradlew :kwk-2:assembleRelease
if errorlevel 1 goto error
copy /Y "kwk-2\build\outputs\apk\release\*.apk" "release_apks\"
if errorlevel 1 goto copy_error

echo.
echo Building kwk-3...
call gradlew :kwk-3:assembleRelease
if errorlevel 1 goto error
copy /Y "kwk-3\build\outputs\apk\release\*.apk" "release_apks\"
if errorlevel 1 goto copy_error

echo.
echo Building kwk-4...
call gradlew :kwk-4:assembleRelease
if errorlevel 1 goto error
copy /Y "kwk-4\build\outputs\apk\release\*.apk" "release_apks\"
if errorlevel 1 goto copy_error

echo.
echo Building kwk-5...
call gradlew :kwk-5:assembleRelease
if errorlevel 1 goto error
copy /Y "kwk-5\build\outputs\apk\release\*.apk" "release_apks\"
if errorlevel 1 goto copy_error

echo.
echo Building kwk-6...
call gradlew :kwk-6:assembleRelease
if errorlevel 1 goto error
copy /Y "kwk-6\build\outputs\apk\release\*.apk" "release_apks\"
if errorlevel 1 goto copy_error

echo.
echo Building kwk-7...
call gradlew :kwk-7:assembleRelease
if errorlevel 1 goto error
copy /Y "kwk-7\build\outputs\apk\release\*.apk" "release_apks\"
if errorlevel 1 goto copy_error

echo.
echo Building kwk-8...
call gradlew :kwk-8:assembleRelease
if errorlevel 1 goto error
copy /Y "kwk-8\build\outputs\apk\release\*.apk" "release_apks\"
if errorlevel 1 goto copy_error

echo.
echo All projects built successfully!
echo.
echo All APK files are copied to: release_apks\
dir /B "release_apks\*.apk"
goto end

:error
echo.
echo Build failed!
pause
exit /b 1

:copy_error
echo.
echo Failed to copy APK files!
pause
exit /b 1

:end
pause 