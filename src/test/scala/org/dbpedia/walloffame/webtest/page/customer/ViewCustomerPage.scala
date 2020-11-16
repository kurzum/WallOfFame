/*
 * This code is in the public domain and may be used in any way you see fit, with the following conditions:
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 */

package org.dbpedia.walloffame.webtest.page.customer

import org.dbpedia.walloffame.webtest.page.{HomePage, Page}
import org.openqa.selenium.WebDriver

class ViewCustomerPage(driver: WebDriver) extends Page[ViewCustomerPage]("customer-view", driver) {
  def deleteCustomer() = click(linkText("Delete the Customer")).expecting[HomePage]

  def edit() = click(linkText("Edit the Customer")).expecting[EditCustomerPage]
}