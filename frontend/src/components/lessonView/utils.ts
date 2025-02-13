export function trimPath(url) {
    return url.substring(url.lastIndexOf("/") + 1);
}